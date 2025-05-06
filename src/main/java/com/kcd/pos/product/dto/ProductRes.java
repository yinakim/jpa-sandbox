package com.kcd.pos.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kcd.pos.common.dto.BaseDto;
import com.kcd.pos.product.domain.BgColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
@Schema(description = "상품 정보 응답 DTO")
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

    @Schema(name = "상품 옵션그룹 목록")
    private List<OptionGroupRes> optionGroupResList;

    @Builder
    public ProductRes(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            String productCd, String productNm, int price, BgColor bgColor, String taxYn, String storeId, CategoryRes category, List<OptionGroupRes> optionGroupResList) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.productCd = productCd;
        this.productNm = productNm;
        this.price = price;
        this.bgColor = bgColor;
        this.taxYn = taxYn;
        this.storeId = storeId;
        this.category = category;
        this.optionGroupResList = Objects.isNull(optionGroupResList) ? Collections.emptyList() : optionGroupResList;
    }
}
