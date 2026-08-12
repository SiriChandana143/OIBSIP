package com.smartlib.service;

import com.smartlib.dto.*;
import com.smartlib.entity.*;
import com.smartlib.exception.*;
import com.smartlib.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToResponse(book);
    }

    public List<BookResponse> searchBooks(String query, Long userId) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> {
                SearchHistory history = SearchHistory.builder()
                        .user(user)
                        .searchQuery(query)
                        .build();
                searchHistoryRepository.save(history);
            });
        }
        return bookRepository.searchBooks(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .category(category)
                .publisher(request.getPublisher())
                .publicationYear(request.getPublicationYear())
                .language(request.getLanguage() != null ? request.getLanguage() : "English")
                .quantity(request.getQuantity() != null ? request.getQuantity() : 1)
                .availableQuantity(request.getQuantity() != null ? request.getQuantity() : 1)
                .imageUrl(request.getImageUrl())
                .build();

        return mapToResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getPublisher() != null) book.setPublisher(request.getPublisher());
        if (request.getPublicationYear() != null) book.setPublicationYear(request.getPublicationYear());
        if (request.getLanguage() != null) book.setLanguage(request.getLanguage());
        if (request.getImageUrl() != null) book.setImageUrl(request.getImageUrl());

        if (request.getQuantity() != null) {
            int diff = request.getQuantity() - book.getQuantity();
            book.setQuantity(request.getQuantity());
            book.setAvailableQuantity(Math.max(0, book.getAvailableQuantity() + diff));
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            book.setCategory(category);
        }

        return mapToResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    private BookResponse mapToResponse(Book book) {
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
}
