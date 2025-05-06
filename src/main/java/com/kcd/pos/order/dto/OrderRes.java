package com.kcd.pos.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name = "주문 조회 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRes {
    @Schema(description = "주문 ID", example = "1")
    private Long orderId;

    @Schema(description = "원가 (할인 전 총액)", example = "15000")
    private int originPrice;

    @Schema(description = "할인 금액", example = "3000")
    private int discountPrice;

    @Schema(description = "총 결제 금액", example = "12000")
    private int totalPrice;

    @Schema(description = "할인 정보")
    private DiscountRes discount;

    @Schema(description = "주문 항목 목록")
    private List<OrderItemRes> orderItems;
}
