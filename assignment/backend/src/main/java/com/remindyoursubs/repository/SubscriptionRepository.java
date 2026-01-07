package com.remindyoursubs.repository;

import com.remindyoursubs.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    List<Subscription> findByUserIdAndIsActive(Long userId, Boolean isActive);

    @Query("SELECT COALESCE(SUM(s.price), 0) FROM Subscription s WHERE s.userId = :userId AND s.isActive = true")
    BigDecimal calculateMonthlyExpenseByUserId(@Param("userId") Long userId);

    Long countByUserIdAndIsActive(Long userId, Boolean isActive);
}
