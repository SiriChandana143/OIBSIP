package com.smartlib.repository;

import com.smartlib.entity.Fine;
import com.smartlib.entity.PaymentStatus;
import com.smartlib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByUser(User user);
    List<Fine> findByUserId(Long userId);
    List<Fine> findByPaymentStatus(PaymentStatus status);
}
