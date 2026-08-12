package com.smartlib.service;

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
class BorrowServiceTest {

    @Mock private BorrowRecordRepository borrowRecordRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private FineRepository fineRepository;
    @Mock private NotificationRepository notificationRepository;
    @Mock private ReservationRepository reservationRepository;

    @InjectMocks private BorrowService borrowService;

    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).name("Test User").email("test@test.com").role(Role.USER).build();
        book = Book.builder().id(1L).title("Test Book").author("Author").availableQuantity(3).quantity(5).build();
    }

    @Test
    void borrowBook_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.findByUserAndStatus(user, BorrowStatus.ISSUED)).thenReturn(List.of());
        when(bookRepository.save(any())).thenReturn(book);
        when(borrowRecordRepository.save(any())).thenAnswer(inv -> {
            BorrowRecord r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        var response = borrowService.borrowBook(1L, 1L);
        assertNotNull(response);
        assertEquals("Test Book", response.getBookTitle());
        verify(bookRepository).save(argThat(b -> b.getAvailableQuantity() == 2));
    }

    @Test
    void borrowBook_NotAvailable_ThrowsException() {
        book.setAvailableQuantity(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BadRequestException.class, () -> borrowService.borrowBook(1L, 1L));
    }
}
