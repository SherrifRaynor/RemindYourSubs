package com.remindyoursubs.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;
    private Long userId;
    private Long subscriptionId;
    private String type;
    private String message;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
