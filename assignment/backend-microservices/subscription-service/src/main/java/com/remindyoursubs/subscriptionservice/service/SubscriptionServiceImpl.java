package com.remindyoursubs.subscriptionservice.service;

import com.remindyoursubs.subscriptionservice.dto.MonthlyExpenseDTO;
import com.remindyoursubs.subscriptionservice.dto.SubscriptionRequestDTO;
import com.remindyoursubs.subscriptionservice.dto.SubscriptionResponseDTO;
import com.remindyoursubs.subscriptionservice.exception.ResourceNotFoundException;
import com.remindyoursubs.subscriptionservice.model.Subscription;
import com.remindyoursubs.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        return convertToDTO(subscription);
    }

    @Override
    public List<SubscriptionResponseDTO> getSubscriptionsByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MonthlyExpenseDTO getMonthlyExpense(Long userId) {
        BigDecimal totalExpense = subscriptionRepository.calculateMonthlyExpenseByUserId(userId);
        Long activeCount = subscriptionRepository.countByUserIdAndIsActive(userId, true);

        MonthlyExpenseDTO dto = new MonthlyExpenseDTO();
        dto.setUserId(userId);
        dto.setTotalExpense(totalExpense != null ? totalExpense : BigDecimal.ZERO);
        dto.setTotalActiveSubscriptions(activeCount != null ? activeCount.intValue() : 0);

        return dto;
    }

    @Override
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

    @Override
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

    @Override
    public SubscriptionResponseDTO toggleReminder(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));

        subscription.setReminderEnabled(!subscription.getReminderEnabled());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDTO(updatedSubscription);
    }

    @Override
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
