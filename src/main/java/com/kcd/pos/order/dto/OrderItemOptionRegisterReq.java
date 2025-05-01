package com.kcd.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderItemOptionRegisterReq {
    @NotNull(message = "옵션 ID는 필수입니다.")
    private final Long optionId;

    @JsonCreator
    public OrderItemOptionRegisterReq(@JsonProperty("optionId") Long optionId) {
        this.optionId = optionId;
    }
}
