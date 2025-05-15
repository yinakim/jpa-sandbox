package com.pos.order.repository;

import com.pos.order.domain.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, Long> {
    @Query("""
                SELECT o FROM OrderMaster o
                WHERE (:orderId IS NULL OR o.orderId = :orderId)
                  AND (
                      (:from IS NULL OR :to IS NULL)
                      OR o.createdAt BETWEEN :from AND :to
                  )
            """)
    List<OrderMaster> findAllByConditions(@Param("orderId") Long orderId,
                                          @Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);
}
