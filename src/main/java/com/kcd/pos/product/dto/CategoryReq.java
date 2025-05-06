package com.kcd.pos.product.dto;

import com.kcd.pos.common.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(name = "카테고리 요청 DTO")
public class CategoryReq extends BaseDto {

    @Schema(name = "카테고리명", example = "음료")
    private String categoryNm;

    @Builder
    public CategoryReq(String categoryNm, String modifiedBy, LocalDateTime modifiedAt) {
        super(modifiedAt, modifiedBy);
        this.categoryNm = categoryNm;
    }
}
