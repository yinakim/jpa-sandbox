package com.pos.order.domain;

import com.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_ITEM_OPTION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItemOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_option_id", nullable = false)
    private Long orderItemOptionId;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "option_nm", nullable = false, length = 50)
    private String optionNm;

    @Column(name = "extra_price", nullable = false)
    private int extraPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Builder
    public OrderItemOption(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            Long orderItemOptionId, Long optionId, String optionNm, int extraPrice, OrderItem orderItem) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.orderItemOptionId = orderItemOptionId;
        this.optionId = optionId;
        this.optionNm = optionNm;
        this.extraPrice = extraPrice;
        this.orderItem = orderItem;
    }

    public void assignToOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
