package com.kcd.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카테고리 응답 DTO")
public class CategoryRes {


    private Long categoryId;
    private String categoryNm;

    private String createdBy;

    @Builder
    public CategoryRes(Long categoryId, String categoryNm) {
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
    }
}
