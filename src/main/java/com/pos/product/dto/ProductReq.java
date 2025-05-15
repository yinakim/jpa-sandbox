package com.pos.product.dto;

import com.pos.product.domain.BgColor;
import com.pos.product.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "상품 검색 요청 DTO")
public class ProductReq {

    @Schema(name = "상품ID", example = "1")
    private Long productId;

    @Schema(name = "카테고리ID", example = "1")
    private Long categoryId;

    @Schema(name = "상품고유번호", example = "P00001")
    private String productCd;

    @Schema(name = "상품명", example = "아메리카노")
    private String productNm;

    @Schema(name = "최소단가")
    private int minPrice;

    @Schema(name = "최대단가")
    private int maxPrice;

    @Schema(description = "배경색", example = "BLUE")
    private BgColor bgColor;

    @Schema(description = "과세여부", example = "Y")
    private String taxYn; // 'Y': 과세, 'N': 비과세

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    private String storeId;

    @Schema(name = "카테고리", example = "음료")
    private Category category;

    @Schema(name = "삭제여부", example = "N")
    private String deleteYn;
}
