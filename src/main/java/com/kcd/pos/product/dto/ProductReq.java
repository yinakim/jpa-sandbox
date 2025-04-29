package com.kcd.pos.product.dto;

import com.kcd.pos.product.domain.BgColor;
import com.kcd.pos.product.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductReq {

    @Schema(name = "상품고유ID", example = "P00001")
    private String productCd;

    @Schema(name = "상품명", example = "아메리카노")
    private String productNm;

    @Schema(name = "단가")
    private int price;

    @Schema(description = "배경색", example = "BLUE")
    private BgColor bgColor;

    @Schema(description = "과세여부")
    private String taxYn; // 'Y': 과세, 'N': 비과세

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    private String storeId;

    @Schema(name = "카테고리", example = "음료")
    private Category category;
}
