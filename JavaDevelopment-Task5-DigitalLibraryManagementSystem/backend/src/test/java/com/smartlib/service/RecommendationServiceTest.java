package com.smartlib.service;

import com.smartlib.entity.*;
import com.smartlib.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BookRepository bookRepository;
    @Mock private BorrowRecordRepository borrowRecordRepository;
    @Mock private SearchHistoryRepository searchHistoryRepository;
    @Mock private RecommendationRepository recommendationRepository;

    @InjectMocks private RecommendationService recommendationService;

    @Test
    void getRecommendations_ReturnsBooks() {
        User user = User.builder().id(1L).name("Test").email("test@test.com").build();
        Category cat = Category.builder().id(1L).categoryName("Programming").build();
        Book book = Book.builder().id(1L).title("Java Book").author("Author")
                .category(cat).availableQuantity(3).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(borrowRecordRepository.findByUser(user)).thenReturn(List.of(
                BorrowRecord.builder().user(user).book(book).build()
        ));
        when(searchHistoryRepository.findTop10ByUserOrderBySearchDateDesc(user)).thenReturn(List.of());
        when(bookRepository.findAll()).thenReturn(List.of(book,
                Book.builder().id(2L).title("Python Book").author("Author2")
                        .category(cat).availableQuantity(2).build()
        ));

        var recommendations = recommendationService.getRecommendations(1L);
        assertNotNull(recommendations);
    }
}
