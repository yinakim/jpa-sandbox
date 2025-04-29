package com.kcd.pos.product.dto;

import com.kcd.pos.common.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductRegisterRes extends BaseDto {

    @Schema(name = "상품고유ID")
    private String productCd;

    @Schema(name = "상품명")
    private String productNm;

    @Schema(name = "상품명")
    private int price;

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    private String storeId;

    @Schema(name = "상품이 속한 카테고리ID")
    private Long categoryId;

    @Schema(name = "상품이 속한 카테고리명")
    private String categoryNm;


    @Builder
    public ProductRegisterRes(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            String productCd, String productNm, int price, String storeId, Long categoryId, String categoryNm) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.productCd = productCd;
        this.productNm = productNm;
        this.price = price;
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
    }
}
