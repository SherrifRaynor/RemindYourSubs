package com.remindyoursubs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method_id", nullable = false)
    private Long paymentMethodId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType; // EXPIRING_SOON, EXPIRED, INACTIVE

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "days_until_expiry")
    private Integer daysUntilExpiry;

    @CreationTimestamp
    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;

    @Column(name = "is_acknowledged")
    private Boolean isAcknowledged = false;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;
}
