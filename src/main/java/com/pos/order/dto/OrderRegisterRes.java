package com.pos.order.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "주문완료 데이터 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({
        "orderId"           // 주문번호
        ,"storeId"          // 매장번호
        ,"posId"            // pos번호

        ,"originPrice"      // 총금액
        ,"discountPrice"    // 할인액
        ,"totalPrice"       // 최종 결제금액

        ,"orderItems"       // 주문항목
        ,"discountType"     // 할인종류
        ,"discountAmount"   // 할인액
        ,"discountPercent"  // 할인율

        ,"orderCreatedAt"   // 주문일시
        ,"orderCreatedBy"   // 주문 담당자
})
public class OrderRegisterRes {

    @Schema(name = "주문번호")
    private Long orderId;

    @Schema(name = "매장ID")
    private String storeId;

    @Schema(name = "포스ID")
    private String posId;


    @Schema(name = "주문항목")
    private List<OrderItemRes> orderItems;      // 주문항목 - 상품가격/수량 , 추가옵션명/추가금액

    @Schema(name = "주문일시")
    private LocalDateTime orderCreatedAt;

    @Schema(name = "담당자")
    private String orderCreatedBy;

    @Schema(name = "할인종류")
    private String discountType;

    @Schema(name = "할인액 (원)")
    private int discountAmount;

    @Schema(name = "할인율 (%)")
    private int discountPercent;


    @Schema(name = "총 금액")
    private int originPrice;

    @Schema(name = "총 할인액")
    private int discountPrice;

    @Schema(name = "최종결제금액")
    private int totalPrice;
}
