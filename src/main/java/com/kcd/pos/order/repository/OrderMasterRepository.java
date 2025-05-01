package com.kcd.pos.order.repository;

import com.kcd.pos.order.domain.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, Long> {
}
