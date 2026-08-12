package com.smartlib.repository;

import com.smartlib.entity.Reservation;
import com.smartlib.entity.ReservationStatus;
import com.smartlib.entity.User;
import com.smartlib.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByBookAndStatus(Book book, ReservationStatus status);
    List<Reservation> findByStatus(ReservationStatus status);
}
