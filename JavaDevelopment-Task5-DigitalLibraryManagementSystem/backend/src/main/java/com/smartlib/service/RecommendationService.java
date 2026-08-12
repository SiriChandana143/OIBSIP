package com.smartlib.service;

import com.smartlib.dto.BookResponse;
import com.smartlib.entity.*;
import com.smartlib.exception.ResourceNotFoundException;
import com.smartlib.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Content-based recommendation engine that analyzes user preferences
 * from borrowing history, search history, and book categories/authors.
 */
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final RecommendationRepository recommendationRepository;

    @Transactional
    public List<BookResponse> getRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Clear old recommendations
        recommendationRepository.deleteByUser(user);

        Map<Long, Double> categoryScores = new HashMap<>();
        Map<String, Double> authorScores = new HashMap<>();
        Set<Long> alreadyBorrowed = new HashSet<>();

        // Analyze borrowing history
        List<BorrowRecord> borrows = borrowRecordRepository.findByUser(user);
        for (BorrowRecord borrow : borrows) {
            Book book = borrow.getBook();
            alreadyBorrowed.add(book.getId());

            if (book.getCategory() != null) {
                categoryScores.merge(book.getCategory().getId(), 3.0, Double::sum);
            }
            authorScores.merge(book.getAuthor().toLowerCase(), 2.0, Double::sum);
        }

        // Analyze search history
        List<SearchHistory> searches = searchHistoryRepository.findTop10ByUserOrderBySearchDateDesc(user);
        for (SearchHistory search : searches) {
            String query = search.getSearchQuery().toLowerCase();
            List<Book> matchedBooks = bookRepository.searchBooks(query);
            for (Book book : matchedBooks) {
                if (book.getCategory() != null) {
                    categoryScores.merge(book.getCategory().getId(), 1.5, Double::sum);
                }
                authorScores.merge(book.getAuthor().toLowerCase(), 1.0, Double::sum);
            }
        }

        // Category affinity mapping for related recommendations
        Map<String, List<String>> categoryAffinity = getCategoryAffinityMap();

        // Score all available books
        List<Book> allBooks = bookRepository.findAll();
        List<ScoredBook> scoredBooks = new ArrayList<>();

        for (Book book : allBooks) {
            if (alreadyBorrowed.contains(book.getId())) continue;

            double score = 0.0;

            // Category match score
            if (book.getCategory() != null) {
                score += categoryScores.getOrDefault(book.getCategory().getId(), 0.0);

                // Related category bonus
                String catName = book.getCategory().getCategoryName();
                for (Map.Entry<Long, Double> entry : categoryScores.entrySet()) {
                    // Find related categories
                    for (Map.Entry<String, List<String>> affinity : categoryAffinity.entrySet()) {
                        if (affinity.getValue().contains(catName)) {
                            score += entry.getValue() * 0.3;
                        }
                    }
                }
            }

            // Author match score
            score += authorScores.getOrDefault(book.getAuthor().toLowerCase(), 0.0);

            // Availability bonus
            if (book.getAvailableQuantity() > 0) {
                score += 0.5;
            }

            // Popularity bonus (based on borrow count)
            long borrowCount = borrows.stream()
                    .filter(b -> b.getBook().getCategory() != null &&
                            b.getBook().getCategory().getId().equals(
                                    book.getCategory() != null ? book.getCategory().getId() : -1L))
                    .count();
            score += borrowCount * 0.2;

            if (score > 0) {
                scoredBooks.add(new ScoredBook(book, score));
            }
        }

        // Sort by score and take top 10
        scoredBooks.sort((a, b) -> Double.compare(b.score, a.score));
        List<ScoredBook> topRecommendations = scoredBooks.stream().limit(10).collect(Collectors.toList());

        // If no personalized recommendations, suggest popular books
        if (topRecommendations.isEmpty()) {
            allBooks.stream()
                    .filter(b -> !alreadyBorrowed.contains(b.getId()) && b.getAvailableQuantity() > 0)
                    .limit(10)
                    .forEach(b -> topRecommendations.add(new ScoredBook(b, 1.0)));
        }

        // Save recommendations
        for (ScoredBook sb : topRecommendations) {
            recommendationRepository.save(Recommendation.builder()
                    .user(user)
                    .book(sb.book)
                    .recommendationScore(sb.score)
                    .build());
        }

        return topRecommendations.stream()
                .map(sb -> mapToResponse(sb.book, sb.score))
                .collect(Collectors.toList());
    }

    private Map<String, List<String>> getCategoryAffinityMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Programming", List.of("Data Science", "Web Development", "Computer Science"));
        map.put("Data Science", List.of("Machine Learning", "Programming", "Mathematics"));
        map.put("Machine Learning", List.of("Artificial Intelligence", "Data Science", "Deep Learning"));
        map.put("Artificial Intelligence", List.of("Machine Learning", "Computer Science"));
        map.put("Web Development", List.of("Programming", "Mobile Development"));
        map.put("Database", List.of("Programming", "Computer Science"));
        return map;
    }

    private BookResponse mapToResponse(Book book, double score) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory() != null ? book.getCategory().getCategoryName() : null)
                .categoryId(book.getCategory() != null ? book.getCategory().getId() : null)
                .publisher(book.getPublisher())
                .publicationYear(book.getPublicationYear())
                .language(book.getLanguage())
                .quantity(book.getQuantity())
                .availableQuantity(book.getAvailableQuantity())
                .imageUrl(book.getImageUrl())
                .available(book.getAvailableQuantity() > 0)
                .build();
    }

    private record ScoredBook(Book book, double score) {}
}
