package com.pos.product.dto;

import com.pos.common.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "키테고리 등록정보 응답 DTO")
public class CategoryRegisterRes extends BaseDto {

    @Schema(name = "카테고리ID")
    private Long categoryId;

    @Schema(name = "카테고리명")
    private String categoryNm;

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    private String storeId;

    @Builder
    public CategoryRegisterRes(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            Long categoryId, String categoryNm, String storeId) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
        this.storeId = storeId;
    }
}
