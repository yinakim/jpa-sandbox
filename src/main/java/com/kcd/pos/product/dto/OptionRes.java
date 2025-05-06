package com.kcd.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "상품 옵션항목 응답 DTO")
public class OptionRes {
    @Schema(name = "옵션ID")
    private Long optionId;
    @Schema(name = "옵션먕")
    private String optionNm;
    @Schema(name = "옵션 추가금액")
    private int extraPrice;
}
