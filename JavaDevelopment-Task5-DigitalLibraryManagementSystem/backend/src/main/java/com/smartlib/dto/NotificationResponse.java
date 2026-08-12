package com.smartlib.dto;

import com.smartlib.entity.NotificationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String message;
    private LocalDateTime createdDate;
    private NotificationStatus status;
}
