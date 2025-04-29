package com.kcd.pos.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kcd.pos.common.dto.BaseDto;
import com.kcd.pos.product.domain.BgColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "productCd"
        ,"productNm"
        ,"price"
        ,"bgColor"
        ,"taxYn"
        ,"storeId"
        ,"category"
        ,"createdBy"
        ,"createdAt"
        ,"modifiedBy"
        ,"modifiedAt"
        ,"createdAtStr"
        ,"modifiedAtStr"
})
public class ProductRes extends BaseDto {

    @Schema(name = "상품고유ID", example = "P00001")
    private String productCd;

    @Schema(name = "상품명", example = "아메리카노")
    private String productNm;

    @Schema(name = "단가", example = "3000")
    private int price;

    @Schema(description = "배경색", example = "BLUE")
    private BgColor bgColor;

    @Schema(description = "과세여부")
    private String taxYn; // 'Y': 과세, 'N': 비과세

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    private String storeId;

    @Schema(name = "상품이 속한 카테고리")
    private CategoryRes category;

    @Builder
    public ProductRes(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            String productCd, String productNm, int price, BgColor bgColor, String taxYn, String storeId, CategoryRes category) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.productCd = productCd;
        this.productNm = productNm;
        this.price = price;
        this.bgColor = bgColor;
        this.taxYn = taxYn;
        this.storeId = storeId;
        this.category = category;
    }
}
