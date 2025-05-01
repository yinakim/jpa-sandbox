package com.kcd.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "옵션 등록정보 응답 DTO")
public class OptionRegisterRes {

    @Schema(name = "옵션ID", example = "1")
    private Long optionId;

    @Schema(name = "옵션명", example = "L", maxLength = 20)
    private String optionNm;

    @Schema(name = "추가 금액", example = "500")
    private int extraPrice;

    @Builder
    public OptionRegisterRes(String optionNm, int extraPrice, String activeYn, Long optionId) {
        this.optionNm = optionNm;
        this.extraPrice = extraPrice;
        this.optionId = optionId;
    }
}