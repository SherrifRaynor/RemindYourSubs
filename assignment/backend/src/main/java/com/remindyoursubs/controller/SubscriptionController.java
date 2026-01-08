package com.remindyoursubs.controller;

import com.remindyoursubs.dto.AnalyticsDTO;
import com.remindyoursubs.dto.MonthlyExpenseDTO;
import com.remindyoursubs.dto.SubscriptionRequestDTO;
import com.remindyoursubs.dto.SubscriptionResponseDTO;
import com.remindyoursubs.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getAllSubscriptions() {
        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getSubscriptionById(@PathVariable Long id) {
        SubscriptionResponseDTO subscription = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResponseDTO>> getSubscriptionsByUserId(@PathVariable Long userId) {
        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/user/{userId}/expense")
    public ResponseEntity<MonthlyExpenseDTO> getMonthlyExpense(@PathVariable Long userId) {
        MonthlyExpenseDTO expense = subscriptionService.getMonthlyExpense(userId);
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/user/{userId}/analytics")
    public ResponseEntity<AnalyticsDTO> getAnalytics(@PathVariable Long userId) {
        AnalyticsDTO analytics = subscriptionService.getAnalytics(userId);
        return ResponseEntity.ok(analytics);
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(
            @Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        SubscriptionResponseDTO createdSubscription = subscriptionService.createSubscription(requestDTO);
        return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> updateSubscription(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        SubscriptionResponseDTO updatedSubscription = subscriptionService.updateSubscription(id, requestDTO);
        return ResponseEntity.ok(updatedSubscription);
    }

    @PutMapping("/{id}/toggle-reminder")
    public ResponseEntity<SubscriptionResponseDTO> toggleReminder(@PathVariable Long id) {
        SubscriptionResponseDTO updatedSubscription = subscriptionService.toggleReminder(id);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
