package com.kcd.pos.product.dto;

import com.kcd.pos.product.domain.BgColor;
import com.kcd.pos.product.domain.Category;
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

    @Schema(name = "expand", example = "false", description = "옵션 포함 응답 여부 (true : 옵션그룹/옵션 포함)")
    private Boolean expand;

    @Builder
    public ProductReq(
            Long productId, Long categoryId, String productCd, String productNm, int minPrice, int maxPrice, BgColor bgColor, String taxYn, String storeId,
            Category category, String deleteYn, Boolean expand) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productCd = productCd;
        this.productNm = productNm;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.bgColor = bgColor;
        this.taxYn = taxYn;
        this.storeId = storeId;
        this.category = category;
        this.deleteYn = deleteYn;
        this.expand = Boolean.TRUE.equals(expand); // 기본값 false -> 옵션그룹 없는 상품목록만 조회 (POS 에서 상품목록만 빠르게 조회)
    }
}
