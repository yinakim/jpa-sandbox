package com.pos.order.domain;

import com.pos.common.constants.DataStatus;
import com.pos.common.constants.DiscountType;
import com.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ORDER_MASTER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "delete_yn = 'N'")
@Getter
@Slf4j
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

    @OneToMany(mappedBy = "orderMaster", cascade = CascadeType.ALL, orphanRemoval = true)
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
        this.discount = Objects.isNull(discount) ? new Discount() : discount;
        this.deleteYn = deleteYn;
    }

    public void safeDelete() {
        this.deleteYn = DataStatus.DELETE_Y;
    }

    public void calculateOrderPrices(int originPriceSum) {
        int finalPrice = 0;
        int discountValue = this.discount.getDiscountValue();

        // 할인적용
        if(this.discount.getDiscountType().name().equals(DiscountType.AMOUNT.name())) {
            // 금액할인
            finalPrice = originPriceSum - discountValue;
        } else {
            // 비율할인
            finalPrice = (int) Math.round(originPriceSum * (1 - discountValue / 100.0));
        }

        log.info("========= 할인계산중 =========");
        // 최종금액 세팅
        this.originPrice = originPriceSum;
        this.discountPrice = originPriceSum - finalPrice;
        this.totalPrice = finalPrice;
        log.info("[주문 - 총액]"+originPriceSum);
        log.info("[주문 - 할인액]"+(originPriceSum - finalPrice));
        log.info("[주문 - 최종 주문금액]"+finalPrice);
        log.info("============================");
    }
}
