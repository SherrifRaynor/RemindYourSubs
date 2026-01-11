package com.remindyoursubs.service;

import com.remindyoursubs.dto.PaymentMethodAnalyticsDTO;
import com.remindyoursubs.dto.PaymentMethodRequestDTO;
import com.remindyoursubs.dto.PaymentMethodResponseDTO;
import com.remindyoursubs.exception.ResourceNotFoundException;
import com.remindyoursubs.model.PaymentAlert;
import com.remindyoursubs.model.PaymentMethod;
import com.remindyoursubs.model.Subscription;
import com.remindyoursubs.repository.PaymentAlertRepository;
import com.remindyoursubs.repository.PaymentMethodRepository;
import com.remindyoursubs.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentAlertRepository paymentAlertRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository,
            PaymentAlertRepository paymentAlertRepository,
            SubscriptionRepository subscriptionRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentAlertRepository = paymentAlertRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    // ========== CRUD Operations ==========

    /**
     * Get all payment methods for a user
     */
    public List<PaymentMethodResponseDTO> getUserPaymentMethods(Long userId) {
        log.info("Fetching payment methods for user: {}", userId);
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByUserId(userId);
        return paymentMethods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a payment method by ID
     */
    public PaymentMethodResponseDTO getPaymentMethodById(Long id) {
        log.info("Fetching payment method: {}", id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found with id: " + id));
        return convertToDTO(paymentMethod);
    }

    /**
     * Create a new payment method
     */
    @Transactional
    public PaymentMethodResponseDTO createPaymentMethod(PaymentMethodRequestDTO requestDTO) {
        log.info("Creating payment method for user: {}", requestDTO.getUserId());

        // If this is being set as default, unset other defaults
        if (Boolean.TRUE.equals(requestDTO.getIsDefault())) {
            unsetOtherDefaults(requestDTO.getUserId(), null);
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setUserId(requestDTO.getUserId());
        paymentMethod.setType(requestDTO.getType());
        paymentMethod.setProvider(requestDTO.getProvider());
        paymentMethod.setLastFourDigits(requestDTO.getLastFourDigits());
        paymentMethod.setNickname(requestDTO.getNickname());
        paymentMethod.setExpiryMonth(requestDTO.getExpiryMonth());
        paymentMethod.setExpiryYear(requestDTO.getExpiryYear());
        paymentMethod.setIsDefault(requestDTO.getIsDefault() != null ? requestDTO.getIsDefault() : false);
        paymentMethod.setIsActive(true);

        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        log.info("Payment method created successfully: {}", saved.getId());

        return convertToDTO(saved);
    }

    /**
     * Update an existing payment method
     */
    @Transactional
    public PaymentMethodResponseDTO updatePaymentMethod(Long id, PaymentMethodRequestDTO requestDTO) {
        log.info("Updating payment method: {}", id);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found with id: " + id));

        // If setting as default, unset other defaults
        if (Boolean.TRUE.equals(requestDTO.getIsDefault()) && !paymentMethod.getIsDefault()) {
            unsetOtherDefaults(paymentMethod.getUserId(), id);
        }

        paymentMethod.setType(requestDTO.getType());
        paymentMethod.setProvider(requestDTO.getProvider());
        paymentMethod.setLastFourDigits(requestDTO.getLastFourDigits());
        paymentMethod.setNickname(requestDTO.getNickname());
        paymentMethod.setExpiryMonth(requestDTO.getExpiryMonth());
        paymentMethod.setExpiryYear(requestDTO.getExpiryYear());
        if (requestDTO.getIsDefault() != null) {
            paymentMethod.setIsDefault(requestDTO.getIsDefault());
        }

        PaymentMethod updated = paymentMethodRepository.save(paymentMethod);
        log.info("Payment method updated successfully: {}", id);

        return convertToDTO(updated);
    }

    /**
     * Delete a payment method
     */
    @Transactional
    public void deletePaymentMethod(Long id) {
        log.info("Deleting payment method: {}", id);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found with id: " + id));

        // Note: Subscriptions will have payment_method_id set to NULL due to ON DELETE
        // SET NULL constraint
        paymentMethodRepository.delete(paymentMethod);
        log.info("Payment method deleted successfully: {}", id);
    }

    // ========== Default Management ==========

    /**
     * Set a payment method as default
     */
    @Transactional
    public PaymentMethodResponseDTO setAsDefault(Long id) {
        log.info("Setting payment method as default: {}", id);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found with id: " + id));

        // Unset other defaults for this user
        unsetOtherDefaults(paymentMethod.getUserId(), id);

        // Set this as default
        paymentMethod.setIsDefault(true);
        PaymentMethod updated = paymentMethodRepository.save(paymentMethod);

        log.info("Payment method set as default successfully: {}", id);
        return convertToDTO(updated);
    }

    /**
     * Unset default flag for all other payment methods of a user
     */
    private void unsetOtherDefaults(Long userId, Long excludeId) {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByUserId(userId);
        for (PaymentMethod pm : paymentMethods) {
            if (pm.getIsDefault() && (excludeId == null || !pm.getId().equals(excludeId))) {
                pm.setIsDefault(false);
                paymentMethodRepository.save(pm);
            }
        }
    }

    // ========== Expiration Management ==========

    /**
     * Get cards expiring within specified days
     */
    public List<PaymentMethodResponseDTO> getExpiringCards(Long userId, int daysThreshold) {
        log.info("Fetching expiring cards for user: {} within {} days", userId, daysThreshold);

        List<PaymentMethod> allPaymentMethods = paymentMethodRepository.findByUserIdAndIsActive(userId, true);

        return allPaymentMethods.stream()
                .filter(pm -> pm.isExpiringSoon(daysThreshold))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check and trigger expiry alerts for a user's payment methods
     */
    @Transactional
    public void checkAndTriggerExpiryAlerts(Long userId) {
        log.info("Checking expiry alerts for user: {}", userId);

        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByUserIdAndIsActive(userId, true);

        for (PaymentMethod pm : paymentMethods) {
            // Check if expired
            if (pm.isExpired()) {
                createAlertIfNotExists(pm, "EXPIRED", "Your " + pm.getProvider() + " card ending in " +
                        pm.getLastFourDigits() + " has expired");
            }
            // Check if expiring within 30 days
            else if (pm.isExpiringSoon(30)) {
                int daysLeft = (int) pm.getDaysUntilExpiry();
                createAlertIfNotExists(pm, "EXPIRING_SOON",
                        "Your " + pm.getProvider() + " card ending in " +
                                pm.getLastFourDigits() + " expires in " + daysLeft + " days");
            }
        }
    }

    /**
     * Create an alert if one doesn't already exist for this payment method
     */
    private void createAlertIfNotExists(PaymentMethod pm, String alertType, String message) {
        // Check if alert already exists (within last 7 days to avoid spam)
        List<PaymentAlert> existingAlerts = paymentAlertRepository.findByPaymentMethodId(pm.getId());

        boolean recentAlertExists = existingAlerts.stream()
                .anyMatch(alert -> alert.getAlertType().equals(alertType) &&
                        alert.getTriggeredAt().isAfter(LocalDateTime.now().minusDays(7)));

        if (!recentAlertExists) {
            PaymentAlert alert = new PaymentAlert();
            alert.setPaymentMethodId(pm.getId());
            alert.setUserId(pm.getUserId());
            alert.setAlertType(alertType);
            alert.setMessage(message);
            alert.setDaysUntilExpiry((int) pm.getDaysUntilExpiry());
            alert.setIsAcknowledged(false);

            paymentAlertRepository.save(alert);
            log.info("Created {} alert for payment method: {}", alertType, pm.getId());
        }
    }

    // ========== Subscription Linking ==========

    /**
     * Get all subscriptions using a specific payment method
     */
    public List<Subscription> getSubscriptionsByPaymentMethod(Long paymentMethodId) {
        log.info("Fetching subscriptions for payment method: {}", paymentMethodId);

        // Verify payment method exists
        paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Payment method not found with id: " + paymentMethodId));

        return subscriptionRepository.findAll().stream()
                .filter(sub -> paymentMethodId.equals(sub.getUserId())) // This will be updated when Subscription has
                                                                        // paymentMethodId
                .collect(Collectors.toList());
    }

    // ========== Analytics ==========

    /**
     * Get payment method analytics for a user
     */
    public PaymentMethodAnalyticsDTO getAnalytics(Long userId) {
        log.info("Generating analytics for user: {}", userId);

        List<PaymentMethod> allMethods = paymentMethodRepository.findByUserId(userId);
        List<PaymentMethod> activeMethods = allMethods.stream()
                .filter(pm -> Boolean.TRUE.equals(pm.getIsActive()))
                .collect(Collectors.toList());

        int expiringCount = (int) activeMethods.stream()
                .filter(pm -> pm.isExpiringSoon(30))
                .count();

        int expiredCount = (int) activeMethods.stream()
                .filter(PaymentMethod::isExpired)
                .count();

        // Calculate subscription distribution by payment method
        List<PaymentMethodAnalyticsDTO.PaymentMethodDistribution> distributions = new ArrayList<>();
        for (PaymentMethod pm : activeMethods) {
            int subCount = countSubscriptionsForPaymentMethod(pm.getId());
            BigDecimal totalAmount = calculateTotalAmountForPaymentMethod(pm.getId());

            if (subCount > 0) {
                PaymentMethodAnalyticsDTO.PaymentMethodDistribution dist = new PaymentMethodAnalyticsDTO.PaymentMethodDistribution(
                        pm.getId(),
                        pm.getNickname() != null ? pm.getNickname() : pm.getProvider(),
                        pm.getMaskedNumber(),
                        subCount,
                        totalAmount);
                distributions.add(dist);
            }
        }

        PaymentMethodAnalyticsDTO analytics = new PaymentMethodAnalyticsDTO();
        analytics.setTotalMethods(allMethods.size());
        analytics.setActiveMethods(activeMethods.size());
        analytics.setExpiringCount(expiringCount);
        analytics.setExpiredCount(expiredCount);
        analytics.setSubscriptionsByMethod(distributions);

        return analytics;
    }

    // ========== Helper Methods ==========

    /**
     * Convert PaymentMethod entity to DTO
     */
    private PaymentMethodResponseDTO convertToDTO(PaymentMethod pm) {
        int subscriptionCount = countSubscriptionsForPaymentMethod(pm.getId());

        PaymentMethodResponseDTO dto = new PaymentMethodResponseDTO();
        dto.setId(pm.getId());
        dto.setUserId(pm.getUserId());
        dto.setType(pm.getType());
        dto.setProvider(pm.getProvider());
        dto.setMaskedNumber(pm.getMaskedNumber());
        dto.setNickname(pm.getNickname());
        dto.setExpiryDate(pm.getFormattedExpiryDate());
        dto.setIsDefault(pm.getIsDefault());
        dto.setIsActive(pm.getIsActive());
        dto.setIsExpired(pm.isExpired());
        dto.setIsExpiringSoon(pm.isExpiringSoon(30));
        dto.setDaysUntilExpiry(pm.getDaysUntilExpiry() < Integer.MAX_VALUE ? (int) pm.getDaysUntilExpiry() : null);
        dto.setSubscriptionCount(subscriptionCount);

        return dto;
    }

    /**
     * Count subscriptions for a payment method
     */
    private int countSubscriptionsForPaymentMethod(Long paymentMethodId) {
        // TODO: Update this once Subscription entity has paymentMethodId field
        // For now, return 0 as subscriptions don't have payment method link yet
        return 0;
    }

    /**
     * Calculate total monthly amount for subscriptions using this payment method
     */
    private BigDecimal calculateTotalAmountForPaymentMethod(Long paymentMethodId) {
        // TODO: Update this once Subscription entity has paymentMethodId field
        return BigDecimal.ZERO;
    }
}
