package com.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "옵션 등록요청 DTO")
public class OptionRegisterReq {

    @Schema(name = "옵션명", example = "L", maxLength = 20)
    private String optionNm;

    @Schema(name = "추가 금액", example = "500")
    private int extraPrice;

    @Builder
    public OptionRegisterReq(String optionNm, int extraPrice) {
        this.optionNm = optionNm;
        this.extraPrice = extraPrice;
    }
}
