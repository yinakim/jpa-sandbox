package com.kcd.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DiscountReq {
    @Schema(description = "할인유형", example = "PERCENT/AMOUNT")
    @NotNull(message = "할인유형은 필수입니다.")
    private final String discountType;

    @Schema(description = "할인값", example = "30% 할인 시 30 / 1000원 할인 시 1000")
    @NotNull(message = "할인값은 필수입니다.")
    @Min(0)
    private final Integer discountValue; // 금액말고, 30% 면 30, 1000원이면 1000

    @JsonCreator
    public DiscountReq(@JsonProperty("discountType") String discountType,
                       @JsonProperty("discountValue") Integer discountValue) {
        this.discountType = discountType;
        this.discountValue = discountValue;
    }
}
