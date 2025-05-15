package com.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "주문 항목 등록요청 DTO")
public class OrderItemRegisterReq {

    @Schema(description = "주문상품ID", example = "1")
    @NotNull(message = "주문상품 ID는 필수입니다.")
    private final Long productId;

    @Schema(description = "주문상품명", example = "아메리카노")
    @NotNull(message = "주문상품명은 필수입니다.")
    private final String productNm;

    @Schema(description = "주문항목 수량(itemQuantity)", example = "1")
    @NotNull(message = "주문항목 수량(itemQuantity)은 필수입니다.")
    @Min(value = 1, message = "주문항목수량(itemQuantity)은 최소 1개 입니다.")
    private final Integer itemQuantity; // 주문항목(item)수량

    @Schema(description = "주문항목 옵션목록")
    private List<OrderItemOptionRegisterReq> itemOptions = new ArrayList<>();

    @JsonCreator
    public OrderItemRegisterReq(@JsonProperty("productId") Long productId,
                                @JsonProperty("productNm") String productNm,
                                @JsonProperty("itemQuantity") Integer itemQuantity,
                                @JsonProperty("itemOptions") List<OrderItemOptionRegisterReq> itemOptions) {
        this.productId = productId;
        this.productNm = productNm;
        this.itemQuantity = itemQuantity;
        this.itemOptions = itemOptions;
    }
}
