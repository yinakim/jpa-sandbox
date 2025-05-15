package com.pos.product.domain;

import com.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OPTION_GROUP")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup extends BaseEntity { // Product -> OptionGrp(1:N) -> Option(1:N) / ProductOptionGroup join

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_grp_id", nullable = false)
    private Long optionGrpId;

    @Column(name = "option_grp_nm", nullable = false, length = 100)
    private String optionGrpNm;

    @Column(name = "min_select_cnt", nullable = false)
    private int minSelectCnt = 1;

    @Column(name = "max_select_cnt", nullable = false)
    private int maxSelectCnt = 3;

    @Column(name = "active_yn", nullable = false, length = 1)
    private String activeYn = "N"; // 최종 상품등록 전 저장된 데이터의 경우 N으로 저장, 최종 상품 등록 시 Y로 저장

    @Column(name = "require_yn", nullable = false, length = 1)
    private String requireYn = "Y"; // 필수선택여부, Y: 필수옵션그룹, N: 선택옵션그룹 - 일관성 고려하여 boolean 사용하지 않고 YN 사용

    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N"; // 삭제여부 safeDelete용

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @Builder
    public OptionGroup(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            Long optionGrpId, String optionGrpNm, int minSelectCnt, int maxSelectCnt, String activeYn, String requireYn, String deleteYn,
            List<Option> options) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.optionGrpId = optionGrpId;
        this.optionGrpNm = optionGrpNm;
        this.minSelectCnt = minSelectCnt;
        this.maxSelectCnt = maxSelectCnt;
        this.activeYn = activeYn;
        this.requireYn = requireYn;
        this.deleteYn = deleteYn;
        this.options = options;
    }

    public void safeDelete() {
        this.deleteYn = "Y";
    }

    public void recovery(){
        this.deleteYn = "N";
    }
}
