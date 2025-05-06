package com.kcd.pos.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kcd.pos.common.dto.BaseDto;
import com.kcd.pos.product.domain.BgColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({
        "productCd"
        ,"productNm"
        ,"price"
        ,"bgColor"
        ,"taxYn"
        ,"storeId"
        ,"categoryId"
        ,"categoryNm"
        ,"createdBy"
        ,"createdAt"
        ,"modifiedBy"
        ,"modifiedAt"
        ,"createdAtStr"
        ,"modifiedAtStr"
})
@Schema(description = "상품 등록정보 응답 DTO")
public class ProductRegisterRes extends BaseDto {

    @Schema(name = "상품고유ID")
    private String productCd;

    @Schema(name = "상품명")
    private String productNm;

    @Schema(name = "상품명")
    private int price;

    @Schema(description = "배경색", example = "BLUE")
    private BgColor bgColor;

    @Schema(description = "과세여부")
    private String taxYn; // 'Y': 과세, 'N': 비과세

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    private String storeId;

    @Schema(name = "상품이 속한 카테고리ID")
    private Long categoryId;

    @Schema(name = "상품이 속한 카테고리명")
    private String categoryNm;

    @Schema(name = "상품에 등록된 옵션그룹")
    private List<OptionGroupRegisterRes> optionGroups;

    @Builder
    public ProductRegisterRes(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            String productCd, String productNm, int price, BgColor bgColor, String taxYn, String storeId, Long categoryId, String categoryNm, List<OptionGroupRegisterRes> optionGroups) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.productCd = productCd;
        this.productNm = productNm;
        this.price = price;
        this.bgColor = bgColor;
        this.taxYn = taxYn;
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
        this.optionGroups = optionGroups;
    }
}
