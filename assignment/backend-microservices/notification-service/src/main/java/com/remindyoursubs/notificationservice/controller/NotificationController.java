package com.remindyoursubs.notificationservice.controller;

import com.remindyoursubs.notificationservice.dto.EmailRequestDTO;
import com.remindyoursubs.notificationservice.dto.NotificationResponseDTO;
import com.remindyoursubs.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Send email notification
     * POST /api/v1/notifications/send
     */
    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> sendNotification(
            @Valid @RequestBody EmailRequestDTO emailRequest,
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {

        if (apiKey == null || apiKey.isBlank()) {
            log.error("API Key is missing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        NotificationResponseDTO notification = notificationService.sendEmailNotification(emailRequest, apiKey);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    /**
     * Get all notifications for a user
     * GET /api/v1/notifications/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(@PathVariable Long userId) {
        List<NotificationResponseDTO> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications for a user
     * GET /api/v1/notifications/user/{userId}/unread
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications(@PathVariable Long userId) {
        List<NotificationResponseDTO> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Mark notification as read
     * PUT /api/v1/notifications/{id}/read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable Long id) {
        NotificationResponseDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }
}
