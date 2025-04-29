package com.kcd.pos.product.dto;

import com.kcd.pos.product.domain.BgColor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Schema(description = "상품등록 요청 DTO")
public class ProductRegisterReq {

    @Schema(description = "상품명", example = "아메리카노")
    @NotBlank(message = "상품명은 필수입니다.")
    private String productNm;

    @Schema(description = "상품 가격", example = "1500")
    @NotNull(message = "상품 가격은 필수입니다. 입력하지 않으면 0원으로 등록됩니다.")
    private int price;

    @Schema(description = "배경색", example = "BLUE")
    private BgColor bgColor;

    @Schema(description = "과세여부")
    private String taxYn; // 'Y': 과세, 'N': 비과세

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    @NotNull(message = "매장 ID는 필수입니다.")
    private String storeId;

    @Schema(description = "카테고리 ID", example = "1")
    @NotNull(message = "카테고리 ID는 필수입니다.")
    private Long categoryId;

    @Builder
    public ProductRegisterReq(String productNm, int price, BgColor bgColor, String taxYn, String storeId, Long categoryId) {
        this.productNm = productNm;
        this.price = Objects.nonNull(price) ? price : 0; // 실제로 포스에서 0원짜리 상품 등록해서 사용 많이 하므로 기본값 0 세팅
        this.bgColor = bgColor;
        this.taxYn = taxYn;
        this.storeId = storeId;
        this.categoryId = categoryId;
    }

}
