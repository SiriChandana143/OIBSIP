package com.smartlib.service;

import com.smartlib.dto.*;
import com.smartlib.entity.*;
import com.smartlib.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI Library Assistant chatbot that processes natural language queries
 * and connects with book database, user records, and recommendation system.
 */
@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;
    private final RecommendationService recommendationService;

    public ChatResponse processMessage(ChatRequest request) {
        String message = request.getMessage().toLowerCase().trim();
        Long userId = request.getUserId();

        // Intent detection
        if (containsAny(message, "find", "search", "show", "list", "book about", "books on", "books about")) {
            return handleBookSearch(message);
        }
        if (containsAny(message, "borrowed", "my books", "issued", "currently reading")) {
            return handleBorrowedBooks(userId);
        }
        if (containsAny(message, "return", "due date", "when should", "when do i")) {
            return handleDueDateQuery(userId);
        }
        if (containsAny(message, "recommend", "suggestion", "suggest")) {
            return handleRecommendations(userId);
        }
        if (containsAny(message, "fine", "penalty", "overdue")) {
            return handleFineQuery(userId);
        }
        if (containsAny(message, "hello", "hi", "hey", "help")) {
            return handleGreeting();
        }
        if (containsAny(message, "available", "in stock", "how many")) {
            return handleAvailability(message);
        }

        return ChatResponse.builder()
                .reply("I'm your SMARTLIB AI assistant! I can help you with:\n" +
                       "• Finding books (e.g., 'Find machine learning books')\n" +
                       "• Checking borrowed books (e.g., 'Show my borrowed books')\n" +
                       "• Due dates (e.g., 'When should I return my book?')\n" +
                       "• Recommendations (e.g., 'Recommend programming books')\n" +
                       "• Fine information (e.g., 'Show my fines')\n" +
                       "Just ask me anything about the library!")
                .build();
    }

    private ChatResponse handleBookSearch(String message) {
        String[] keywords = {"find", "search", "show", "list", "book about", "books on", "books about", "book", "books"};
        String query = message;
        for (String kw : keywords) {
            query = query.replace(kw, "").trim();
        }

        if (query.isEmpty()) {
            List<BookResponse> allBooks = bookRepository.findAll().stream()
                    .limit(5)
                    .map(this::mapBook)
                    .collect(Collectors.toList());
            return ChatResponse.builder()
                    .reply("Here are some popular books in our library:")
                    .suggestedBooks(allBooks)
                    .build();
        }

        List<BookResponse> books = bookRepository.searchBooks(query).stream()
                .map(this::mapBook)
                .limit(8)
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            return ChatResponse.builder()
                    .reply("Sorry, I couldn't find any books matching \"" + query + "\". Try different keywords or browse our categories.")
                    .build();
        }

        return ChatResponse.builder()
                .reply("I found " + books.size() + " book(s) related to \"" + query + "\":")
                .suggestedBooks(books)
                .build();
    }

    private ChatResponse handleBorrowedBooks(Long userId) {
        if (userId == null) {
            return ChatResponse.builder().reply("Please log in to view your borrowed books.").build();
        }

        List<BorrowRecord> borrows = borrowRecordRepository.findByUserId(userId).stream()
                .filter(b -> b.getStatus() == BorrowStatus.ISSUED || b.getStatus() == BorrowStatus.OVERDUE)
                .collect(Collectors.toList());

        if (borrows.isEmpty()) {
            return ChatResponse.builder()
                    .reply("You don't have any books borrowed currently. Browse our collection to find something interesting!")
                    .build();
        }

        StringBuilder reply = new StringBuilder("You currently have " + borrows.size() + " book(s) borrowed:\n\n");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");

        for (BorrowRecord b : borrows) {
            reply.append("📖 ").append(b.getBook().getTitle())
                 .append(" by ").append(b.getBook().getAuthor())
                 .append("\n   Due: ").append(b.getDueDate().format(fmt))
                 .append(" (").append(b.getStatus()).append(")\n\n");
        }

        return ChatResponse.builder().reply(reply.toString()).build();
    }

    private ChatResponse handleDueDateQuery(Long userId) {
        if (userId == null) {
            return ChatResponse.builder().reply("Please log in to check your due dates.").build();
        }

        List<BorrowRecord> active = borrowRecordRepository.findByUserId(userId).stream()
                .filter(b -> b.getStatus() == BorrowStatus.ISSUED || b.getStatus() == BorrowStatus.OVERDUE)
                .collect(Collectors.toList());

        if (active.isEmpty()) {
            return ChatResponse.builder().reply("You have no active borrows.").build();
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        StringBuilder reply = new StringBuilder("Here are your due dates:\n\n");

        for (BorrowRecord b : active) {
            String status = b.getStatus() == BorrowStatus.OVERDUE ? "⚠️ OVERDUE" : "✅ On time";
            reply.append("📖 ").append(b.getBook().getTitle())
                 .append("\n   Due: ").append(b.getDueDate().format(fmt))
                 .append(" - ").append(status).append("\n\n");
        }

        reply.append("Remember: Fine is ₹5 per overdue day.");
        return ChatResponse.builder().reply(reply.toString()).build();
    }

    private ChatResponse handleRecommendations(Long userId) {
        if (userId == null) {
            return ChatResponse.builder().reply("Please log in to get personalized recommendations.").build();
        }

        List<BookResponse> recommendations = recommendationService.getRecommendations(userId);

        if (recommendations.isEmpty()) {
            return ChatResponse.builder()
                    .reply("Start borrowing or searching books to get personalized recommendations!")
                    .build();
        }

        return ChatResponse.builder()
                .reply("Based on your reading history, here are my top recommendations for you:")
                .suggestedBooks(recommendations.stream().limit(5).collect(Collectors.toList()))
                .build();
    }

    private ChatResponse handleFineQuery(Long userId) {
        if (userId == null) {
            return ChatResponse.builder().reply("Please log in to check your fines.").build();
        }

        List<Fine> fines = fineRepository.findByUserId(userId).stream()
                .filter(f -> f.getPaymentStatus() == PaymentStatus.PENDING)
                .collect(Collectors.toList());

        if (fines.isEmpty()) {
            return ChatResponse.builder().reply("Great news! You have no pending fines. 🎉").build();
        }

        StringBuilder reply = new StringBuilder("You have " + fines.size() + " pending fine(s):\n\n");
        for (Fine f : fines) {
            reply.append("📕 ").append(f.getBorrowRecord().getBook().getTitle())
                 .append(" - ₹").append(f.getAmount()).append("\n");
        }
        reply.append("\nPlease visit the library to pay your fines.");

        return ChatResponse.builder().reply(reply.toString()).build();
    }

    private ChatResponse handleGreeting() {
        return ChatResponse.builder()
                .reply("Hello! 👋 Welcome to SMARTLIB AI!\n\n" +
                       "I'm your intelligent library assistant. I can help you:\n" +
                       "• 🔍 Search for books\n" +
                       "• 📚 Check your borrowed books\n" +
                       "• 📅 View due dates\n" +
                       "• ⭐ Get personalized recommendations\n" +
                       "• 💰 Check fines\n\n" +
                       "What would you like to know?")
                .build();
    }

    private ChatResponse handleAvailability(String message) {
        long available = bookRepository.countByAvailableQuantityGreaterThan(0);
        long total = bookRepository.count();
        return ChatResponse.builder()
                .reply("We currently have " + available + " books available out of " + total + " total books in our library.")
                .build();
    }

    private boolean containsAny(String message, String... keywords) {
        for (String kw : keywords) {
            if (message.contains(kw)) return true;
        }
        return false;
    }

    private BookResponse mapBook(com.smartlib.entity.Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory() != null ? book.getCategory().getCategoryName() : null)
                .availableQuantity(book.getAvailableQuantity())
                .imageUrl(book.getImageUrl())
                .available(book.getAvailableQuantity() > 0)
                .build();
    }
}
