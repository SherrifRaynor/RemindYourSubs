package com.remindyoursubs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {

    private Long id;
    private Long userId;
    private String name;
    private BigDecimal price;
    private Integer billingDate;
    private Boolean isActive;
    private Boolean reminderEnabled;
    private LocalDate lastReminderSent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
