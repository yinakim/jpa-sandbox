package com.kcd.pos.order.domain;

import com.kcd.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_ITEM_OPTION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "delete_yn = 'N'")
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

    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Builder
    public OrderItemOption(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            Long orderItemOptionId, Long optionId, String optionNm, int extraPrice, OrderItem orderItem, String deleteYn) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.orderItemOptionId = orderItemOptionId;
        this.optionId = optionId;
        this.optionNm = optionNm;
        this.extraPrice = extraPrice;
        this.orderItem = orderItem;
        this.deleteYn = deleteYn;
    }

    public void assignToOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
