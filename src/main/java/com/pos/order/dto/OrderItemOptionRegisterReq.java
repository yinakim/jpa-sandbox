package com.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "주문 항목 옵션 등록정보 요청 DTO")
public class OrderItemOptionRegisterReq {
    @NotNull(message = "옵션 ID는 필수입니다.")
    private final Long optionId;

    @JsonCreator
    public OrderItemOptionRegisterReq(@JsonProperty("optionId") Long optionId) {
        this.optionId = optionId;
    }
}
