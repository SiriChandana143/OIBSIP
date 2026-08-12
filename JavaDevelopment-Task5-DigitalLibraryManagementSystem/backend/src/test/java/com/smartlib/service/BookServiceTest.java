package com.smartlib.service;

import com.smartlib.dto.*;
import com.smartlib.entity.*;
import com.smartlib.exception.*;
import com.smartlib.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private SearchHistoryRepository searchHistoryRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        Category cat = Category.builder().id(1L).categoryName("Programming").build();
        sampleBook = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .category(cat)
                .quantity(5)
                .availableQuantity(5)
                .build();
    }

    @Test
    void getAllBooks_ReturnsList() {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));
        List<BookResponse> books = bookService.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
    }

    @Test
    void getBookById_NotFound_ThrowsException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void createBook_DuplicateIsbn_ThrowsException() {
        BookRequest request = BookRequest.builder()
                .title("Test")
                .author("Author")
                .isbn("978-0132350884")
                .build();

        when(bookRepository.existsByIsbn("978-0132350884")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> bookService.createBook(request));
    }

    @Test
    void searchBooks_ReturnsMatchingBooks() {
        when(bookRepository.searchBooks("java")).thenReturn(List.of(sampleBook));
        List<BookResponse> results = bookService.searchBooks("java", null);
        assertEquals(1, results.size());
    }
}
