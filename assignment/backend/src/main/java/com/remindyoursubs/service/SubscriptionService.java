package com.remindyoursubs.service;

import com.remindyoursubs.dto.AnalyticsDTO;
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
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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

    public AnalyticsDTO getAnalytics(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);

        // Calculate monthly trend for last 6 months
        List<AnalyticsDTO.MonthlyTrendDTO> monthlyTrend = calculateMonthlyTrend(subscriptions);

        // Calculate upcoming bills (next 30 days)
        List<AnalyticsDTO.UpcomingBillDTO> upcomingBills = calculateUpcomingBills(subscriptions);

        // Calculate subscription distribution by price
        AnalyticsDTO.SubscriptionDistributionDTO distribution = calculateDistribution(subscriptions);

        return new AnalyticsDTO(monthlyTrend, upcomingBills, distribution);
    }

    private List<AnalyticsDTO.MonthlyTrendDTO> calculateMonthlyTrend(List<Subscription> subscriptions) {
        List<AnalyticsDTO.MonthlyTrendDTO> trends = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Generate last 6 months
        for (int i = 5; i >= 0; i--) {
            LocalDate month = today.minusMonths(i);
            String monthLabel = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " "
                    + month.getYear();

            // For simplicity, we'll use current active subscriptions
            // In a real app, you'd track historical data
            double totalExpense = subscriptions.stream()
                    .filter(Subscription::getIsActive)
                    .mapToDouble(s -> s.getPrice().doubleValue())
                    .sum();

            int count = (int) subscriptions.stream()
                    .filter(Subscription::getIsActive)
                    .count();

            trends.add(new AnalyticsDTO.MonthlyTrendDTO(monthLabel, totalExpense, count));
        }

        return trends;
    }

    private List<AnalyticsDTO.UpcomingBillDTO> calculateUpcomingBills(List<Subscription> subscriptions) {
        LocalDate today = LocalDate.now();

        return subscriptions.stream()
                .filter(Subscription::getIsActive)
                .map(sub -> {
                    LocalDate nextBilling = sub.getNextBillingDate();
                    long daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, nextBilling);

                    return new AnalyticsDTO.UpcomingBillDTO(
                            sub.getId(),
                            sub.getName(),
                            sub.getPrice().doubleValue(),
                            nextBilling.getDayOfMonth(),
                            (int) daysUntil);
                })
                .filter(bill -> bill.getDaysUntil() <= 30 && bill.getDaysUntil() >= 0)
                .sorted(Comparator.comparingInt(AnalyticsDTO.UpcomingBillDTO::getDaysUntil))
                .limit(5)
                .collect(Collectors.toList());
    }

    // Removed calculateDaysUntil method - now using full dates

    private AnalyticsDTO.SubscriptionDistributionDTO calculateDistribution(List<Subscription> subscriptions) {
        int lowPrice = 0; // < 100,000
        int mediumPrice = 0; // 100,000 - 250,000
        int highPrice = 0; // > 250,000

        for (Subscription sub : subscriptions) {
            if (!sub.getIsActive())
                continue;

            double price = sub.getPrice().doubleValue();
            if (price < 100000) {
                lowPrice++;
            } else if (price <= 250000) {
                mediumPrice++;
            } else {
                highPrice++;
            }
        }

        return new AnalyticsDTO.SubscriptionDistributionDTO(lowPrice, mediumPrice, highPrice);
    }

    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO requestDTO) {
        Subscription subscription = new Subscription();
        subscription.setUserId(requestDTO.getUserId());
        subscription.setName(requestDTO.getName());
        subscription.setPrice(requestDTO.getPrice());
        subscription.setNextBillingDate(requestDTO.getNextBillingDate());
        subscription
                .setReminderTiming(requestDTO.getReminderTiming() != null ? requestDTO.getReminderTiming() : "1_DAY");
        subscription.setReminderCustomMinutes(requestDTO.getReminderCustomMinutes());
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
        subscription.setNextBillingDate(requestDTO.getNextBillingDate());
        subscription.setReminderTiming(requestDTO.getReminderTiming());
        subscription.setReminderCustomMinutes(requestDTO.getReminderCustomMinutes());
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
        dto.setNextBillingDate(subscription.getNextBillingDate());
        dto.setReminderTiming(subscription.getReminderTiming());
        dto.setReminderCustomMinutes(subscription.getReminderCustomMinutes());
        dto.setIsActive(subscription.getIsActive());
        dto.setReminderEnabled(subscription.getReminderEnabled());
        dto.setLastReminderSent(subscription.getLastReminderSent());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());
        return dto;
    }
}
