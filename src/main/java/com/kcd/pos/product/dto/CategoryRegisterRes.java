package com.kcd.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRegisterRes {

    @Schema(name = "카테고리ID")
    private Long categoryId;

    @Schema(name = "카테고리명")
    private String categoryNm;

    @Builder
    public CategoryRegisterRes(Long categoryId, String categoryNm) {
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
    }
}
