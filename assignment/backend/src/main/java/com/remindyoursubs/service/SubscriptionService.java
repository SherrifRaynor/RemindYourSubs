package com.remindyoursubs.service;

import com.remindyoursubs.dto.MonthlyExpenseDTO;
import com.remindyoursubs.dto.SubscriptionRequestDTO;
import com.remindyoursubs.dto.SubscriptionResponseDTO;
import com.remindyoursubs.exception.ResourceNotFoundException;
import com.remindyoursubs.model.Subscription;
import com.remindyoursubs.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        return convertToDTO(subscription);
    }

    public List<SubscriptionResponseDTO> getSubscriptionsByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MonthlyExpenseDTO getMonthlyExpense(Long userId) {
        BigDecimal totalExpense = subscriptionRepository.calculateMonthlyExpenseByUserId(userId);
        Long activeCount = subscriptionRepository.countByUserIdAndIsActive(userId, true);

        MonthlyExpenseDTO dto = new MonthlyExpenseDTO();
        dto.setUserId(userId);
        dto.setTotalExpense(totalExpense != null ? totalExpense : BigDecimal.ZERO);
        dto.setTotalActiveSubscriptions(activeCount != null ? activeCount.intValue() : 0);

        return dto;
    }

    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO requestDTO) {
        Subscription subscription = new Subscription();
        subscription.setUserId(requestDTO.getUserId());
        subscription.setName(requestDTO.getName());
        subscription.setPrice(requestDTO.getPrice());
        subscription.setBillingDate(requestDTO.getBillingDate());
        subscription.setIsActive(requestDTO.getIsActive() != null ? requestDTO.getIsActive() : true);
        subscription
                .setReminderEnabled(requestDTO.getReminderEnabled() != null ? requestDTO.getReminderEnabled() : true);

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(savedSubscription);
    }

    public SubscriptionResponseDTO updateSubscription(Long id, SubscriptionRequestDTO requestDTO) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));

        subscription.setUserId(requestDTO.getUserId());
        subscription.setName(requestDTO.getName());
        subscription.setPrice(requestDTO.getPrice());
        subscription.setBillingDate(requestDTO.getBillingDate());
        subscription.setIsActive(requestDTO.getIsActive());
        subscription.setReminderEnabled(requestDTO.getReminderEnabled());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(updatedSubscription);
    }

    public SubscriptionResponseDTO toggleReminder(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));

        subscription.setReminderEnabled(!subscription.getReminderEnabled());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(updatedSubscription);
    }

    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        subscriptionRepository.delete(subscription);
    }

    private SubscriptionResponseDTO convertToDTO(Subscription subscription) {
        SubscriptionResponseDTO dto = new SubscriptionResponseDTO();
        dto.setId(subscription.getId());
        dto.setUserId(subscription.getUserId());
        dto.setName(subscription.getName());
        dto.setPrice(subscription.getPrice());
        dto.setBillingDate(subscription.getBillingDate());
        dto.setIsActive(subscription.getIsActive());
        dto.setReminderEnabled(subscription.getReminderEnabled());
        dto.setLastReminderSent(subscription.getLastReminderSent());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());
        return dto;
    }
}
