package com.smartlib.service;

import com.smartlib.dto.AnalyticsResponse;
import com.smartlib.entity.*;
import com.smartlib.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final FineRepository fineRepository;

    public AnalyticsResponse getAnalytics() {
        return AnalyticsResponse.builder()
                .totalBooks(bookRepository.count())
                .totalUsers(userRepository.count())
                .issuedBooks(borrowRecordRepository.countByStatus(BorrowStatus.ISSUED))
                .availableBooks(bookRepository.countByAvailableQuantityGreaterThan(0))
                .overdueBooks(borrowRecordRepository.countByStatus(BorrowStatus.OVERDUE))
                .totalFines(fineRepository.count())
                .pendingFines(fineRepository.findByPaymentStatus(PaymentStatus.PENDING).size())
                .monthlyBorrows(getMonthlyBorrowCounts())
                .build();
    }

    private List<Long> getMonthlyBorrowCounts() {
        int year = LocalDate.now().getYear();
        List<Long> counts = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            final int m = month;
            long count = borrowRecordRepository.findAll().stream()
                    .filter(b -> b.getIssueDate() != null
                            && b.getIssueDate().getYear() == year
                            && b.getIssueDate().getMonthValue() == m)
                    .count();
            counts.add(count);
        }
        return counts;
    }
}
