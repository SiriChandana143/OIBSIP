package com.smartlib.repository;

import com.smartlib.entity.Notification;
import com.smartlib.entity.NotificationStatus;
import com.smartlib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedDateDesc(User user);
    List<Notification> findByUserIdOrderByCreatedDateDesc(Long userId);
    List<Notification> findByUserAndStatus(User user, NotificationStatus status);
}
