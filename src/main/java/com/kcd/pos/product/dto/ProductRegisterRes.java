package com.kcd.pos.product.dto;

import com.kcd.pos.product.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRegisterRes {

    @Schema(name = "상품 PK ID")
    private String productId;

    @Schema(name = "상품명")
    private String productNm;

    @Schema(name = "상품명")
    private int price;

    @Schema(name = "상품이 속한 카테고리ID")
    private Long categoryId;

    @Schema(name = "상품이 속한 카테고리명")
    private String categoryNm;


    @Builder
    public ProductRegisterRes(String productId, String productNm, int price, Long categoryId, String categoryNm) {
        this.productId = productId;
        this.productNm = productNm;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
    }
}
