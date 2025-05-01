package com.kcd.pos.order.domain;

import com.kcd.pos.common.constants.DataStatus;
import com.kcd.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDER_ITEM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "delete_yn = 'N'")
@Getter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_nm", nullable = false, length = 100)
    private String productNm;

    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N";

    @Column(name = "item_price", nullable = false)
    private int itemPrice; // product.price를 가져오긴 하지만 별도 단가 저장하기 위해 itemPrice로 명명

    @Column(name = "item_quantity", nullable = false)
    @Min(value = 1, message = "주문수량은 최소 1개 이상")
    private int itemQuantity; // 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderMaster orderMaster;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemOption> orderItemOptions = new ArrayList<>();

    @Builder
    public OrderItem(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            Long orderItemId, Long productId, String productNm, int itemPrice, int itemQuantity, OrderMaster orderMaster, String deleteYn

    ) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.itemQuantity = itemQuantity;
        this.productNm = productNm;
        this.itemPrice = itemPrice;
        this.orderMaster = orderMaster;
        this.deleteYn = deleteYn;
    }

    public void assignToOrders(OrderMaster orderMaster) {
        this.orderMaster = orderMaster;
    }

    public void addOrderItemOption(OrderItemOption option) {
        this.orderItemOptions.add(option); // list에 하나씩 추가
        option.assignToOrderItem(this); // 자식 -> 부모 할당(연관관계 설정)
    }

    public void safeDelete() {
        this.deleteYn = DataStatus.DELETE_Y;
    }

    public void changeItemQuantity(int newQuantity) {
        if (newQuantity < 1) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        this.itemQuantity = newQuantity;
    }
}
