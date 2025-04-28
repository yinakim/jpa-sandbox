package com.kcd.pos.product.dto;

import com.kcd.pos.product.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductReq {

    @Schema(name = "상품고유ID", example = "P00001")
    private String productId;

    @Schema(name = "상품명", example = "아메리카노")
    private String productNm;

    @Schema(name = "단가")
    private int price;

    @Schema(name = "카테고리", example = "음료")
    private Category category;
}
