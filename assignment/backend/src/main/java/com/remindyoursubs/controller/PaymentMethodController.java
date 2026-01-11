package com.remindyoursubs.controller;

import com.remindyoursubs.dto.PaymentMethodAnalyticsDTO;
import com.remindyoursubs.dto.PaymentMethodRequestDTO;
import com.remindyoursubs.dto.PaymentMethodResponseDTO;
import com.remindyoursubs.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    /**
     * Get all payment methods for a user
     * GET /api/v1/payment-methods/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentMethodResponseDTO>> getUserPaymentMethods(@PathVariable Long userId) {
        log.info("GET /api/v1/payment-methods/user/{}", userId);
        List<PaymentMethodResponseDTO> paymentMethods = paymentMethodService.getUserPaymentMethods(userId);
        return ResponseEntity.ok(paymentMethods);
    }

    /**
     * Get a payment method by ID
     * GET /api/v1/payment-methods/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethodById(@PathVariable Long id) {
        log.info("GET /api/v1/payment-methods/{}", id);
        PaymentMethodResponseDTO paymentMethod = paymentMethodService.getPaymentMethodById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    /**
     * Create a new payment method
     * POST /api/v1/payment-methods
     */
    @PostMapping
    public ResponseEntity<PaymentMethodResponseDTO> createPaymentMethod(
            @Valid @RequestBody PaymentMethodRequestDTO requestDTO) {
        log.info("POST /api/v1/payment-methods - Creating for user: {}", requestDTO.getUserId());
        PaymentMethodResponseDTO createdPaymentMethod = paymentMethodService.createPaymentMethod(requestDTO);
        return new ResponseEntity<>(createdPaymentMethod, HttpStatus.CREATED);
    }

    /**
     * Update an existing payment method
     * PUT /api/v1/payment-methods/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodResponseDTO> updatePaymentMethod(
            @PathVariable Long id,
            @Valid @RequestBody PaymentMethodRequestDTO requestDTO) {
        log.info("PUT /api/v1/payment-methods/{}", id);
        PaymentMethodResponseDTO updatedPaymentMethod = paymentMethodService.updatePaymentMethod(id, requestDTO);
        return ResponseEntity.ok(updatedPaymentMethod);
    }

    /**
     * Delete a payment method
     * DELETE /api/v1/payment-methods/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        log.info("DELETE /api/v1/payment-methods/{}", id);
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Set a payment method as default
     * PUT /api/v1/payment-methods/{id}/set-default
     */
    @PutMapping("/{id}/set-default")
    public ResponseEntity<PaymentMethodResponseDTO> setAsDefault(@PathVariable Long id) {
        log.info("PUT /api/v1/payment-methods/{}/set-default", id);
        PaymentMethodResponseDTO paymentMethod = paymentMethodService.setAsDefault(id);
        return ResponseEntity.ok(paymentMethod);
    }

    /**
     * Get cards expiring soon for a user
     * GET /api/v1/payment-methods/user/{userId}/expiring?days=30
     */
    @GetMapping("/user/{userId}/expiring")
    public ResponseEntity<List<PaymentMethodResponseDTO>> getExpiringCards(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "30") int days) {
        log.info("GET /api/v1/payment-methods/user/{}/expiring?days={}", userId, days);
        List<PaymentMethodResponseDTO> expiringCards = paymentMethodService.getExpiringCards(userId, days);
        return ResponseEntity.ok(expiringCards);
    }

    /**
     * Get analytics for user's payment methods
     * GET /api/v1/payment-methods/user/{userId}/analytics
     */
    @GetMapping("/user/{userId}/analytics")
    public ResponseEntity<PaymentMethodAnalyticsDTO> getAnalytics(@PathVariable Long userId) {
        log.info("GET /api/v1/payment-methods/user/{}/analytics", userId);
        PaymentMethodAnalyticsDTO analytics = paymentMethodService.getAnalytics(userId);
        return ResponseEntity.ok(analytics);
    }

    /**
     * Trigger expiry alert check for a user
     * POST /api/v1/payment-methods/user/{userId}/check-alerts
     */
    @PostMapping("/user/{userId}/check-alerts")
    public ResponseEntity<Void> checkExpiryAlerts(@PathVariable Long userId) {
        log.info("POST /api/v1/payment-methods/user/{}/check-alerts", userId);
        paymentMethodService.checkAndTriggerExpiryAlerts(userId);
        return ResponseEntity.ok().build();
    }
}
