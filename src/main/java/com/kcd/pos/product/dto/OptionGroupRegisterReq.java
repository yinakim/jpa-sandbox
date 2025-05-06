package com.kcd.pos.product.dto;

import com.kcd.pos.product.domain.Option;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(name = "옵션그룹 등록요청 DTO")
public class OptionGroupRegisterReq {
    @Schema(name = "옵션 그룹명", example = "사이즈", maxLength = 20)
    @NotBlank(message = "옵션 그룹명은 필수입니다.")
    @Size(min = 1, max = 20, message = "옵션 그룹명은 공백포함 1~20글자여야 합니다")
    private String optionGrpNm;

    @NotBlank
    @Schema(name = "사용여부", example = "Y/N", defaultValue = "N",
            description = "Y: 등록완료, N: 등록중")
    private String activeYn = "N"; // 최종 상품등록 전 저장된 데이터의 경우 N으로 저장, 최종 상품 등록 시 Y로 저장

    @NotBlank
    @Schema(name = "필수/선택 여부", example = "Y/N", defaultValue = "Y",
            description = "Y: 필수옵션그룹, N: 선택옵션그룹")
    private String requireYn = "Y"; // 일관성 고려하여 boolean 사용하지 않고 YN 사용

    @Valid // option 유효성체크
    @NotEmpty(message = "옵션 목록은 최소 1개 이상이어야 합니다.")
    private List<OptionRegisterReq> options = new ArrayList<>(); // 옵션이 없는 옵션그룹은 없음

    @Schema(name = "추가옵션 수 - 최소선택개수", example = "1")
    @Min(value = 1, message = "최소선택개수, 최소 1개 필수")
    private int minSelectCnt = 1; // 최소 1개 필수

    @Schema(name = "추가옵션 수 - 최대선택개수", example = "1")
    @Min(value = 1, message = "최대선택개수, 최소값과 작거나 큰 값")
    private int maxSelectCnt = 1;

    @AssertTrue(message = "최소 선택 수는 최대 선택 수보다 작거나 같아야 합니다.") // 프론트에서 체크 해서 보내겠지만 DTO에서 한번 더 체크
    public boolean isSelectCountValid() {
        return minSelectCnt <= maxSelectCnt;
    }

    @Builder
    public OptionGroupRegisterReq(String optionGrpNm, String storeId, String activeYn, String requireYn, int minSelectCnt, int maxSelectCnt) {
        this.optionGrpNm = optionGrpNm;
        this.activeYn = activeYn;
        this.requireYn = requireYn;
        this.minSelectCnt = minSelectCnt;
        this.maxSelectCnt = maxSelectCnt;
    }
}
