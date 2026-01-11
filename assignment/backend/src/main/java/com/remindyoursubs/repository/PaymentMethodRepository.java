package com.remindyoursubs.repository;

import com.remindyoursubs.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    /**
     * Find all payment methods for a user with active filter
     */
    List<PaymentMethod> findByUserIdAndIsActive(Long userId, Boolean isActive);

    /**
     * Find all payment methods for a user (active and inactive)
     */
    List<PaymentMethod> findByUserId(Long userId);

    /**
     * Find the default payment method for a user
     */
    Optional<PaymentMethod> findByUserIdAndIsDefault(Long userId, Boolean isDefault);

    /**
     * Find cards expiring before a certain year/month
     * Used for detecting expiring cards
     */
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.userId = :userId " +
            "AND pm.isActive = true " +
            "AND ((pm.expiryYear = :year AND pm.expiryMonth <= :month) " +
            "OR pm.expiryYear < :year)")
    List<PaymentMethod> findExpiringCards(@Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);

    /**
     * Count active payment methods for a user
     */
    @Query("SELECT COUNT(pm) FROM PaymentMethod pm WHERE pm.userId = :userId AND pm.isActive = true")
    Long countActiveByUserId(@Param("userId") Long userId);
}
