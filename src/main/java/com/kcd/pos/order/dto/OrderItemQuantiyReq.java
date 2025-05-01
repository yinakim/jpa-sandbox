package com.kcd.pos.order.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemQuantiyReq {
    @Min(0)
    private int itemQuantity;
}
