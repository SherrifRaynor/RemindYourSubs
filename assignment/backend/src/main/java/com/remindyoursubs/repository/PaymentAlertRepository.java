package com.remindyoursubs.repository;

import com.remindyoursubs.model.PaymentAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentAlertRepository extends JpaRepository<PaymentAlert, Long> {

    /**
     * Find all alerts for a user with acknowledgment filter
     */
    List<PaymentAlert> findByUserIdAndIsAcknowledged(Long userId, Boolean isAcknowledged);

    /**
     * Find all alerts for a user
     */
    List<PaymentAlert> findByUserId(Long userId);

    /**
     * Find all alerts for a specific payment method
     */
    List<PaymentAlert> findByPaymentMethodId(Long paymentMethodId);
}
