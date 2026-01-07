package com.remindyoursubs.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @NotNull(message = "Billing date is required")
    @Min(value = 1, message = "Billing date must be between 1 and 31")
    @Max(value = 31, message = "Billing date must be between 1 and 31")
    private Integer billingDate;

    private Boolean isActive = true;
    private Boolean reminderEnabled = true;
}
