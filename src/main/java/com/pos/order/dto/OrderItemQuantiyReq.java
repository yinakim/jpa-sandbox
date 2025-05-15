package com.pos.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "주문 항목 수량변경 요청 DTO")
public class OrderItemQuantiyReq {
    @Min(0)
    private int itemQuantity;
}
