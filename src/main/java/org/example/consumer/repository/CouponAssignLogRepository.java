package org.example.consumer.repository;

import org.example.consumer.domain.CouponAssignLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponAssignLogRepository extends JpaRepository<CouponAssignLog, Long> {
}
