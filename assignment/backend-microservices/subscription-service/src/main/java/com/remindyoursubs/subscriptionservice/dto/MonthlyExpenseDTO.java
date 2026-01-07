package com.remindyoursubs.subscriptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyExpenseDTO {

    private Long userId;
    private BigDecimal totalExpense;
    private Integer totalActiveSubscriptions;
}
