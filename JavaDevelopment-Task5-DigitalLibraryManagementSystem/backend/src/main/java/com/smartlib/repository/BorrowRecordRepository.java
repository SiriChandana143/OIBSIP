package com.smartlib.repository;

import com.smartlib.entity.BorrowRecord;
import com.smartlib.entity.BorrowStatus;
import com.smartlib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUser(User user);
    List<BorrowRecord> findByUserId(Long userId);
    List<BorrowRecord> findByStatus(BorrowStatus status);
    long countByStatus(BorrowStatus status);
    List<BorrowRecord> findByUserAndStatus(User user, BorrowStatus status);
}
