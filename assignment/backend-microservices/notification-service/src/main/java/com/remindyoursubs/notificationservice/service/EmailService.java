package com.remindyoursubs.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Service for sending emails via Resend API
 */
@Service
@Slf4j
public class EmailService {

    @Value("${notification.resend.api.url}")
    private String resendApiUrl;

    @Value("${notification.from.email}")
    private String fromEmail;

    private final WebClient webClient;

    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Send email using Resend API
     * 
     * @param apiKey      Resend API key
     * @param to          Recipient email
     * @param subject     Email subject
     * @param htmlContent HTML content
     * @return Response from Resend API
     */
    public Mono<Map<String, Object>> sendEmail(String apiKey, String to, String subject, String htmlContent) {
        log.info("Sending email to: {} with subject: {}", to, subject);

        Map<String, Object> emailRequest = Map.of(
                "from", fromEmail,
                "to", new String[] { to },
                "subject", subject,
                "html", htmlContent);

        return webClient.post()
                .uri(resendApiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(emailRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnSuccess(response -> log.info("Email sent successfully: {}", response))
                .doOnError(error -> log.error("Failed to send email: {}", error.getMessage()));
    }

    /**
     * Generate reminder email HTML
     */
    public String generateReminderEmailHtml(String subscriptionName, int daysUntilBilling, String price) {
        return String.format("""
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #2563eb; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background-color: #f9fafb; }
                        .footer { padding: 20px; text-align: center; font-size: 12px; color: #6b7280; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🔔 Subscription Reminder</h1>
                        </div>
                        <div class="content">
                            <h2>Your %s subscription payment is approaching!</h2>
                            <p><strong>Subscription:</strong> %s</p>
                            <p><strong>Amount:</strong> Rp %s</p>
                            <p><strong>Days until billing:</strong> %d days</p>
                            <p>Make sure you have sufficient funds in your account.</p>
                        </div>
                        <div class="footer">
                            <p>This is an automated reminder from RemindYourSubs</p>
                        </div>
                    </div>
                </body>
                </html>
                """, subscriptionName, subscriptionName, price, daysUntilBilling);
    }
}
