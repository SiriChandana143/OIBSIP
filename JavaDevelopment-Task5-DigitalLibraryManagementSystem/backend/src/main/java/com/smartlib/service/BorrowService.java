package com.smartlib.service;

import com.smartlib.dto.*;
import com.smartlib.entity.*;
import com.smartlib.exception.*;
import com.smartlib.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;
    private final NotificationRepository notificationRepository;
    private final ReservationRepository reservationRepository;

    @Value("${app.borrow-days:14}")
    private int borrowDays;

    @Value("${app.fine-per-day:5}")
    private int finePerDay;

    @Transactional
    public BorrowResponse borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailableQuantity() <= 0) {
            throw new BadRequestException("Book is not available. Consider making a reservation.");
        }

        List<BorrowRecord> activeBorrows = borrowRecordRepository.findByUserAndStatus(user, BorrowStatus.ISSUED);
        if (activeBorrows.size() >= 5) {
            throw new BadRequestException("Maximum 5 books can be borrowed at a time");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        LocalDateTime now = LocalDateTime.now();
        BorrowRecord record = BorrowRecord.builder()
                .user(user)
                .book(book)
                .issueDate(now)
                .dueDate(now.plusDays(borrowDays))
                .status(BorrowStatus.ISSUED)
                .build();

        record = borrowRecordRepository.save(record);

        notificationRepository.save(Notification.builder()
                .user(user)
                .message("You borrowed \"" + book.getTitle() + "\". Due date: " + record.getDueDate().toLocalDate())
                .build());

        return mapToResponse(record);
    }

    @Transactional
    public BorrowResponse returnBook(Long borrowId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new BadRequestException("Book already returned");
        }

        LocalDateTime now = LocalDateTime.now();
        record.setReturnDate(now);
        record.setStatus(BorrowStatus.RETURNED);

        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        if (now.isAfter(record.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), now);
            BigDecimal fineAmount = BigDecimal.valueOf(overdueDays * finePerDay);

            fineRepository.save(Fine.builder()
                    .user(record.getUser())
                    .borrowRecord(record)
                    .amount(fineAmount)
                    .paymentStatus(PaymentStatus.PENDING)
                    .build());

            notificationRepository.save(Notification.builder()
                    .user(record.getUser())
                    .message("Overdue fine of ₹" + fineAmount + " for \"" + book.getTitle() + "\"")
                    .build());
        }

        // Fulfill pending reservations
        List<Reservation> pendingReservations = reservationRepository
                .findByBookAndStatus(book, ReservationStatus.PENDING);
        if (!pendingReservations.isEmpty()) {
            Reservation first = pendingReservations.get(0);
            first.setStatus(ReservationStatus.FULFILLED);
            reservationRepository.save(first);
            notificationRepository.save(Notification.builder()
                    .user(first.getUser())
                    .message("Reserved book \"" + book.getTitle() + "\" is now available!")
                    .build());
        }

        return mapToResponse(borrowRecordRepository.save(record));
    }

    public List<BorrowResponse> getUserBorrows(Long userId) {
        return borrowRecordRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BorrowResponse> getAllBorrows() {
        return borrowRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BorrowResponse> getActiveBorrows() {
        return borrowRecordRepository.findByStatus(BorrowStatus.ISSUED).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void markOverdueBooks() {
        List<BorrowRecord> issued = borrowRecordRepository.findByStatus(BorrowStatus.ISSUED);
        LocalDateTime now = LocalDateTime.now();
        for (BorrowRecord record : issued) {
            if (now.isAfter(record.getDueDate())) {
                record.setStatus(BorrowStatus.OVERDUE);
                borrowRecordRepository.save(record);
            }
        }
    }

    private BorrowResponse mapToResponse(BorrowRecord record) {
        return BorrowResponse.builder()
                .id(record.getId())
                .userId(record.getUser().getId())
                .userName(record.getUser().getName())
                .bookId(record.getBook().getId())
                .bookTitle(record.getBook().getTitle())
                .bookAuthor(record.getBook().getAuthor())
                .issueDate(record.getIssueDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .status(record.getStatus())
                .build();
    }
}
