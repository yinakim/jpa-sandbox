package com.kcd.pos.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "주문 할인 정보 응답 DTO")
public class DiscountRes {

    @Schema(description = "할인 타입", example = "PERCENT", allowableValues = {"PERCENT", "AMOUNT"})
    private String discountType;

    @Schema(description = "입력 받은 할인 값", example = "10")
    private int discountValue;

    @Schema(description = "적용된 퍼센트 (%)", example = "10")
    private int discountPercent;

    @Schema(description = "적용된 할인 금액", example = "3000")
    private int discountAmount;
}