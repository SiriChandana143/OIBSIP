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
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public ReservationResponse createReservation(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailableQuantity() > 0) {
            throw new BadRequestException("Book is available. You can borrow it directly.");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .status(ReservationStatus.PENDING)
                .build();

        reservation = reservationRepository.save(reservation);

        notificationRepository.save(Notification.builder()
                .user(user)
                .message("Reservation placed for \"" + book.getTitle() + "\". We'll notify you when available.")
                .build());

        return mapToResponse(reservation);
    }

    public List<ReservationResponse> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .userName(reservation.getUser().getName())
                .bookId(reservation.getBook().getId())
                .bookTitle(reservation.getBook().getTitle())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus())
                .build();
    }
}
