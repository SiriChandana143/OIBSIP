package com.smartlib.dto;

import com.smartlib.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long borrowId;
    private String bookTitle;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdDate;
}
