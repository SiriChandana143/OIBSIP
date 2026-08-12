package com.smartlib.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponse {

    private long totalBooks;
    private long totalUsers;
    private long issuedBooks;
    private long availableBooks;
    private long overdueBooks;
    private long totalFines;
    private long pendingFines;
    private java.util.List<Long> monthlyBorrows;
}
