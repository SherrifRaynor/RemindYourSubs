package com.remindyoursubs.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Subscription name is required")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @NotNull(message = "Next billing date is required")
    private LocalDate nextBillingDate;

    private String reminderTiming = "1_DAY"; // "1_DAY", "1_HOUR", "30_MIN", "CUSTOM"
    private Integer reminderCustomMinutes; // For CUSTOM timing

    private Boolean isActive = true;
    private Boolean reminderEnabled = true;
}
