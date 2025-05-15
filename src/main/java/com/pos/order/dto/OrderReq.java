package com.pos.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "주문 조회 요청 DTO")
public class OrderReq {
    @Schema(description = "주문 ID (단건 조회용)", example = "1")
    private Long orderId; // null체크

    @Schema(description = "매장 ID", example = "store-uuid-1234")
    private String storeId;

    @Schema(description = "조회 시작일시 (yyyyMMddHHmmss)", example = "20250430000000")
    private LocalDateTime fromDate;

    @Schema(description = "조회 종료일시 (yyyyMMddHHmmss)", example = "20250501235959")
    private LocalDateTime toDate;

    /**
     * 문자열 파라미터를 LocalDateTime으로 변환
     * OrderReq 반환
     */
    public static OrderReq fromOrderReqParam(Long orderId, String storeId, String fromDateStr, String toDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        LocalDateTime from = null;
        LocalDateTime to = null;

        try {
            if (Objects.nonNull(fromDateStr) && !fromDateStr.isBlank()) {
                from = LocalDateTime.parse(fromDateStr, formatter);
            }

            if (Objects.nonNull(toDateStr) && !toDateStr.isBlank()) {
                to = LocalDateTime.parse(toDateStr, formatter);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식(yyyyMMddHHmmss)이 올바르지 않습니다. ex) 20250501235959", e);
        }

        return OrderReq.builder()
                .orderId(orderId)
                .storeId(storeId)
                .fromDate(from)
                .toDate(to)
                .build();
    }
}
