package com.pos.product.domain;

import com.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "OPTION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @Column(name = "option_nm", nullable = false, length = 50)
    private String optionNm;

    @Column(name = "extra_price", nullable = false)
    private int extraPrice;

    @Column(name = "active_yn", nullable = false, length = 1)
    private String activeYn = "N";

    @Column(name = "delete_yn", nullable = false, length = 1)
    private String deleteYn = "N";

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_group_id", nullable = false)
    private OptionGroup optionGroup;

    @Builder

    public Option(
            Long optionId, String optionNm, int extraPrice, OptionGroup optionGroup, String activeYn, String deleteYn,
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt
    ) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.optionId = optionId;
        this.optionNm = optionNm;
        this.extraPrice = extraPrice;
        this.optionGroup = optionGroup;
        this.activeYn = activeYn;
        this.deleteYn = deleteYn;
    }

    public void safeDelete() {
        this.deleteYn = "Y";
    }

    public void recovery(){
        this.deleteYn = "N";
    }
}
