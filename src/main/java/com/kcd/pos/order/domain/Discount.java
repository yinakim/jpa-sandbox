package com.kcd.pos.order.domain;

import com.kcd.pos.common.constants.DiscountType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
public class Discount {

    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "discount_percent")
    private int discountPercent; // ex.10%

    @Column(name = "discount_amount")
    private int discountAmount; // ex.2000원

    @Column(name = "discount_value")
    private int discountValue; // 받아오는 값, this.discountType 에 따라 계산할 값

    // 할인 null 방지
    public Discount() {
        this.discountType = DiscountType.EMPTY;
        this.discountPercent = 0;
        this.discountAmount = 0;
        this.discountValue = 0;
    }

    @Builder
    public Discount(DiscountType discountType, int discountValue, int discountPercent, int discountAmount) {
        if(Objects.isNull(discountType)) {
            this.discountType = DiscountType.EMPTY;
        } else {
            this.discountType = discountType;
        }
        this.discountValue = discountValue;
        // 할인계산은 금액이 있는 order에서 해야 하는데?
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
    }
}
