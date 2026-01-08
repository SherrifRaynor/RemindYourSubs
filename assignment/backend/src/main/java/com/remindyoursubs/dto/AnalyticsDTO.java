package com.remindyoursubs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {

    private List<MonthlyTrendDTO> monthlyTrend;
    private List<UpcomingBillDTO> upcomingBills;
    private SubscriptionDistributionDTO distribution;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyTrendDTO {
        private String month; // Format: "Jan 2026"
        private Double totalExpense;
        private Integer subscriptionCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpcomingBillDTO {
        private Long subscriptionId;
        private String subscriptionName;
        private Double price;
        private Integer billingDate;
        private Integer daysUntil;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionDistributionDTO {
        private Integer lowPrice; // < 100k
        private Integer mediumPrice; // 100k - 250k
        private Integer highPrice; // > 250k
    }
}
