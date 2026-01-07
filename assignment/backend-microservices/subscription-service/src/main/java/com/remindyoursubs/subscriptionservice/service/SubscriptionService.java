package com.remindyoursubs.subscriptionservice.service;

import com.remindyoursubs.subscriptionservice.dto.MonthlyExpenseDTO;
import com.remindyoursubs.subscriptionservice.dto.SubscriptionRequestDTO;
import com.remindyoursubs.subscriptionservice.dto.SubscriptionResponseDTO;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionResponseDTO> getAllSubscriptions();

    SubscriptionResponseDTO getSubscriptionById(Long id);

    List<SubscriptionResponseDTO> getSubscriptionsByUserId(Long userId);

    MonthlyExpenseDTO getMonthlyExpense(Long userId);

    SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO requestDTO);

    SubscriptionResponseDTO updateSubscription(Long id, SubscriptionRequestDTO requestDTO);

    SubscriptionResponseDTO toggleReminder(Long id);

    void deleteSubscription(Long id);
}
