package com.kcd.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRegisterReq {

    @Schema(name = "카테고리명", example = "음료")
    @NotBlank(message = "카테고리명은 필수입니다.")
    private String categoryNm;

    @Builder
    public CategoryRegisterReq(String categoryNm) {
        this.categoryNm = categoryNm;
    }
}
