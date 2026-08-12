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
public class FineService {

    private final FineRepository fineRepository;

    public List<FineResponse> getAllFines() {
        return fineRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FineResponse> getUserFines(Long userId) {
        return fineRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FineResponse markAsPaid(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        fine.setPaymentStatus(PaymentStatus.PAID);
        return mapToResponse(fineRepository.save(fine));
    }

    private FineResponse mapToResponse(Fine fine) {
        return FineResponse.builder()
                .id(fine.getId())
                .userId(fine.getUser().getId())
                .userName(fine.getUser().getName())
                .borrowId(fine.getBorrowRecord().getId())
                .bookTitle(fine.getBorrowRecord().getBook().getTitle())
                .amount(fine.getAmount())
                .paymentStatus(fine.getPaymentStatus())
                .createdDate(fine.getCreatedDate())
                .build();
    }
}
