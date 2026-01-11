package com.remindyoursubs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Payment type is required")
    @Column(nullable = false, length = 50)
    private String type; // CREDIT_CARD, DEBIT_CARD, E_WALLET, BANK_ACCOUNT

    @Column(length = 100)
    private String provider; // Visa, Mastercard, GoPay, OVO, BCA, etc.

    @Pattern(regexp = "\\d{4}", message = "Last four digits must be 4 numbers")
    @Column(name = "last_four_digits", length = 4)
    private String lastFourDigits;

    @Column(length = 100)
    private String nickname;

    @Min(value = 1, message = "Month must be between 1-12")
    @Max(value = 12, message = "Month must be between 1-12")
    @Column(name = "expiry_month")
    private Integer expiryMonth;

    @Min(value = 2024, message = "Year must be valid")
    @Column(name = "expiry_year")
    private Integer expiryYear;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Check if this payment method has expired
     */
    public boolean isExpired() {
        if (expiryYear == null || expiryMonth == null) return false;
        LocalDate expiry = LocalDate.of(expiryYear, expiryMonth, 1).plusMonths(1).minusDays(1);
        return LocalDate.now().isAfter(expiry);
    }

    /**
     * Check if this payment method is expiring soon (within specified days)
     */
    public boolean isExpiringSoon(int daysThreshold) {
        if (expiryYear == null || expiryMonth == null) return false;
        LocalDate expiry = LocalDate.of(expiryYear, expiryMonth, 1).plusMonths(1).minusDays(1);
        LocalDate threshold = LocalDate.now().plusDays(daysThreshold);
        return !LocalDate.now().isAfter(expiry) && threshold.isAfter(expiry);
    }

    /**
     * Get days until expiry (negative if expired)
     */
    public long getDaysUntilExpiry() {
        if (expiryYear == null || expiryMonth == null) return Long.MAX_VALUE;
        LocalDate expiry = LocalDate.of(expiryYear, expiryMonth, 1).plusMonths(1).minusDays(1);
        return ChronoUnit.DAYS.between(LocalDate.now(), expiry);
    }

    /**
     * Get masked card number for display (e.g., "**** **** **** 1234")
     */
    public String getMaskedNumber() {
        if (lastFourDigits == null) return "N/A";
        return "**** **** **** " + lastFourDigits;
    }

    /**
     * Get formatted expiry date (e.g., "12/2026")
     */
    public String getFormattedExpiryDate() {
        if (expiryMonth == null || expiryYear == null) return null;
        return String.format("%02d/%d", expiryMonth, expiryYear);
    }
}
