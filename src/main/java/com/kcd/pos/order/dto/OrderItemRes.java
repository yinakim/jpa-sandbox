package com.kcd.pos.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "주문 항목 정보 응답 DTO")
public class OrderItemRes {
    @Schema(description = "주문 항목 ID", example = "100")
    private Long orderItemId;

    @Schema(description = "상품 ID", example = "5")
    private Long productId;

    @Schema(description = "상품명", example = "아메리카노")
    private String productNm;

    @Schema(description = "단가", example = "5000")
    private int itemPrice;

    @Schema(description = "수량", example = "2")
    private int itemQuantity;

    @Schema(description = "선택된 옵션 목록")
    private List<OrderItemOptionRes> options;
}
