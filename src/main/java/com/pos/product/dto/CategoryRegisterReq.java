package com.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "카테고리 등록요청 DTO")
public class CategoryRegisterReq {

    @Schema(name = "카테고리명", example = "음료")
    @NotBlank(message = "카테고리명은 필수입니다.")
    private String categoryNm;

    @Schema(name = "매장ID", example = "a7f8b9c2-3d5a-4ef7-9c12-8fa2b5ef3a72")
    @NotNull(message = "매장 ID는 필수입니다.")
    private String storeId;

    @Builder
    public CategoryRegisterReq(String categoryNm, String storeId) {
        this.categoryNm = categoryNm;
        this.storeId = storeId;
    }
}
