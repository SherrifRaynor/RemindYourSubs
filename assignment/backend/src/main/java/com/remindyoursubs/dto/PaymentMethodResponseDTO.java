package com.remindyoursubs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodResponseDTO {

    private Long id;
    private Long userId;
    private String type;
    private String provider;
    private String maskedNumber; // **** **** **** 1234
    private String nickname;
    private String expiryDate; // "12/2026"
    private Boolean isDefault;
    private Boolean isActive;
    private Boolean isExpired;
    private Boolean isExpiringSoon; // Within 30 days
    private Integer daysUntilExpiry;
    private Integer subscriptionCount; // Number of subscriptions using this method
}
