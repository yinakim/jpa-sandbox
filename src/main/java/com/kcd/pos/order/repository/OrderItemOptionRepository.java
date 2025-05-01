package com.kcd.pos.order.repository;

import com.kcd.pos.order.domain.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Long> {
}
