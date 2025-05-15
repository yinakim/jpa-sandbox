package com.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pos.common.constants.DiscountType;
import com.pos.order.domain.Discount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Schema(description = "신규주문 등록 전용 요청데이터 DTO")
public class OrderRegisterReq { // 금액관련 필드는 final
    @Schema(description = "원가", example = "10000")
    @NotNull(message = "주문 원가(originPrice)는 필수입니다.")
    @Min(value = 0, message = "주문 원가는 0(원) 이상이어야 합니다.")
    private final Integer originPrice;

    @Schema(description = "할인 금액", example = "3000")
    @Min(value = 0, message = "할인 금액(discountPrice)은 0(원) 이상이어야 합니다.")
    private final Integer discountPrice;

    @Schema(description = "최종금액", example = "7000")
    @NotNull(message = "최종 금액(totalPrice)은 필수입니다.")
    @Min(value = 0, message = "최종 금액은 0(원) 이상이어야 합니다.")
    private final Integer totalPrice;

    @Schema(description = "할인정보")
    private final DiscountReq discount;

    @Schema(description = "주문상품목록")
    @NotNull(message = "주문 항목 목록은 필수입니다.")
    private final List<OrderItemRegisterReq> orderItems;

    @JsonCreator
    public OrderRegisterReq(@JsonProperty("originPrice") Integer originPrice,
                            @JsonProperty("discountPrice") Integer discountPrice,
                            @JsonProperty("totalPrice") Integer totalPrice,
                            @JsonProperty("discount") DiscountReq discount,
                            @JsonProperty("orderItems") List<OrderItemRegisterReq> orderItems
    ) {
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.orderItems = Objects.nonNull(orderItems) ? Collections.unmodifiableList(orderItems) : Collections.emptyList();
    }

    // 할인정보 null 체크, 변환
    public Discount toDiscount() {
        if (Objects.nonNull(this.discount)) {
            return Discount.builder()
                    .discountType(DiscountType.valueOf(this.discount.getDiscountType()))
                    .discountValue(this.discount.getDiscountValue())
                    .build();
        }
        return Discount.empty();
    }
}
