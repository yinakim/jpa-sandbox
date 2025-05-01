package com.kcd.pos.order.domain;

import com.kcd.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDER_MASTER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderMaster extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "origin_price", nullable = false)
    private int originPrice; // 할인전

    @Column(name = "total_price", nullable = false)
    private int totalPrice; // 최종금액

    @Column(name = "discount_price", nullable = false)
    private int discountPrice; // 할인금액 (!= 할인된 금액)

    @Embedded
    private Discount discount;

    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N";


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignToOrders(this); // 자식 -> 부모 할당(연관관계 설정)
    }

    @Builder
    public OrderMaster(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
                  int originPrice, int totalPrice, int discountPrice, Discount discount, String deleteYn) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.originPrice = originPrice;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.discount = discount;
        this.deleteYn = deleteYn;
    }
}
