package com.pos.order.domain;

import com.pos.common.constants.DiscountType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Embeddable
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
    protected Discount() {
        this.discountType = DiscountType.EMPTY;
        this.discountValue = 0;
        this.discountPercent = 0;
        this.discountAmount = 0;
    }

    public static Discount empty(){
        return new Discount();
    }

    @Builder
    public Discount(DiscountType discountType, int discountValue, int discountPercent, int discountAmount) {
        // 할인방법 값 null체크
        this.discountType = Objects.requireNonNullElse(discountType, DiscountType.EMPTY);
        this.discountValue = discountValue;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
    }
}
