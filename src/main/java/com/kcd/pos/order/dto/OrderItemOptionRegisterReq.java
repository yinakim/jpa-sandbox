package com.kcd.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderItemOptionRegisterReq {
    @NotNull(message = "옵션 ID는 필수입니다.")
    private final Long optionId;

    @NotNull(message = "추가 금액은 필수입니다.")
    @Min(value = 0, message = "추가 금액은 0 이상이어야 합니다.")
    private final Integer extraPrice;

    @JsonCreator
    public OrderItemOptionRegisterReq(@JsonProperty("optionId") Long optionId,
                                      @JsonProperty("extraPrice") Integer extraPrice) {
        this.optionId = optionId;
        this.extraPrice = extraPrice;
    }
}
