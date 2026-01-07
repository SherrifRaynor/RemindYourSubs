package com.remindyoursubs.notificationservice.service;

import com.remindyoursubs.notificationservice.dto.EmailRequestDTO;
import com.remindyoursubs.notificationservice.dto.NotificationResponseDTO;
import com.remindyoursubs.notificationservice.model.Notification;
import com.remindyoursubs.notificationservice.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    public NotificationResponseDTO sendEmailNotification(EmailRequestDTO emailRequest, String apiKey) {
        try {
            // Send email via Resend API
            emailService.sendEmail(apiKey, emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getHtml())
                    .subscribe(
                            response -> log.info("Email sent successfully: {}", response),
                            error -> log.error("Failed to send email: {}", error.getMessage()));

            // Save notification to database
            Notification notification = new Notification();
            notification.setUserId(emailRequest.getUserId());
            notification.setSubscriptionId(emailRequest.getSubscriptionId());
            notification.setType("EMAIL");
            notification.setMessage(emailRequest.getSubject());
            notification.setSentAt(LocalDateTime.now());
            notification.setIsRead(false);

            Notification savedNotification = notificationRepository.save(notification);
            return convertToDTO(savedNotification);

        } catch (Exception e) {
            log.error("Error sending email notification: {}", e.getMessage());
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDTO> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NotificationResponseDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);

        return convertToDTO(updatedNotification);
    }

    private NotificationResponseDTO convertToDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setSubscriptionId(notification.getSubscriptionId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setSentAt(notification.getSentAt());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
