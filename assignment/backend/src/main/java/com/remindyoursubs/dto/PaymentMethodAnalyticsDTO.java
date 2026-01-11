package com.remindyoursubs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodAnalyticsDTO {

    private Integer totalMethods;
    private Integer activeMethods;
    private Integer expiringCount; // Expiring within 30 days
    private Integer expiredCount;
    private List<PaymentMethodDistribution> subscriptionsByMethod;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethodDistribution {
        private Long paymentMethodId;
        private String nickname;
        private String maskedNumber;
        private Integer subscriptionCount;
        private BigDecimal totalMonthlyAmount;
    }
}
