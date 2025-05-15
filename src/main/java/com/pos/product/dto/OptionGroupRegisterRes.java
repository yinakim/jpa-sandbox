package com.pos.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "옵션그룹 등록정보 응답 DTO")
public class OptionGroupRegisterRes {

    @Schema(name = "옵션 그룹 ID", example = "사이즈", maxLength = 20)
    private Long optionGrpId;

    @Schema(name = "옵션 그룹명", example = "사이즈", maxLength = 20)
    private String optionGrpNm;

    @Schema(name = "옵션목록")
    private List<OptionRegisterRes> options = new ArrayList<>(); // 옵션ID, 이름만 있는 간단한 res

    @Builder
    public OptionGroupRegisterRes(Long optionGrpId, String optionGrpNm, List<OptionRegisterRes> options) {
        this.optionGrpId = optionGrpId;
        this.optionGrpNm = optionGrpNm;
        this.options = options;
    }
}
