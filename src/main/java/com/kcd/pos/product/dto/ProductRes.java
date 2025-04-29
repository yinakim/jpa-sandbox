package com.kcd.pos.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kcd.pos.common.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "productCd"
        ,"productNm"
        ,"price"
        ,"storeId"
        ,"createdAt"
        ,"createdAtStr"
        ,"modifiedBy"
        ,"modifiedAt"
        ,"modifiedAtStr"
})
public class ProductRes extends BaseDto {

    @Schema(name = "상품고유ID", example = "P00001")
    private String productCd;

    @Schema(name = "상품명", example = "아메리카노")
    private String productNm;

    @Schema(name = "단가", example = "3000")
    private int price;

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    private String storeId;

    @Builder
    public ProductRes(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            String productCd, String productNm, int price, String storeId) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.productCd = productCd;
        this.productNm = productNm;
        this.price = price;
        this.storeId = storeId;
    }
}
