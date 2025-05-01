package com.kcd.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderItemRegisterReq {

    @Schema(description = "주문상품ID", example = "1")
    @NotNull(message = "주문상품 ID는 필수입니다.")
    private final Long productId;

    @Schema(description = "주문상품명", example = "아메리카노")
    @NotNull(message = "주문상품명은 필수입니다.")
    private final String productNm;

    @Schema(description = "주문상품 가격", example = "1500")
    @NotNull(message = "주문상품 가격(itemPrice)은 필수입니다. 입력하지 않으면 0원으로 등록됩니다.")
    @Min(value = 0, message = "주문상품 가격(itemPrice)은 0(원) 이상이어야 합니다.")
    private final Integer itemPrice; // Product테이블의 price와 달라질 수 있으므로 itemPrice로 명명

    @Schema(description = "주문항목 수량(itemQuantity)", example = "1")
    @NotNull(message = "주문항목 수량(itemQuantity)은 필수입니다.")
    @Min(value = 1, message = "주문항목수량(itemQuantity)은 최소 1개 입니다.")
    private final Integer itemQuantity; // 주문항목(item)수량

    @Schema(description = "주문항목 옵션목록")
    private List<OrderItemOptionRegisterReq> itemOptions = new ArrayList<>();

    @JsonCreator
    public OrderItemRegisterReq(@JsonProperty("productId") Long productId,
                                @JsonProperty("productNm") String productNm,
                                @JsonProperty("itemPrice") Integer itemPrice,
                                @JsonProperty("itemQuantity") Integer itemQuantity,
                                @JsonProperty("itemOptions") List<OrderItemOptionRegisterReq> itemOptions) {
        this.productId = productId;
        this.productNm = productNm;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemOptions = itemOptions;
    }
}
