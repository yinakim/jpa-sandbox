package com.pos.order.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "주문 항목별 옵션 정보 응답 DTO")
public class OrderItemOptionRes {
    @Schema(description = "옵션 항목 ID", example = "200")
    private Long orderItemOptionId;

    @Schema(description = "옵션 ID", example = "13")
    private Long optionId;

    @Schema(description = "옵션명", example = "Venti 사이즈")
    private String optionNm;

    @Schema(description = "추가 금액", example = "1000")
    private int extraPrice;
}
