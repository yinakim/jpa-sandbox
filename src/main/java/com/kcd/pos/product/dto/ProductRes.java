package com.kcd.pos.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRes {

    private String productId; // ex. "P00001"
    private String productNm;
    private int price;
}
