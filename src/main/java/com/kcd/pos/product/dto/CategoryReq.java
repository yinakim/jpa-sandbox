package com.kcd.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryReq {

    @Schema(name = "카테고리명", example = "음료")
    private String categoryNm;

}
