package com.pos.product.domain;

import com.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_OPTION_GROUP")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pogId;

    // 상품 테이블의 productId와 join
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    // 옵션그룹 테이블의 productId와 join
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    // 상품 등록 완료 여부 기준으로, 옵션그룹과의 연결 상태를 표현
    @Column(name = "active_yn", length = 1)
    private String activeYn = "N"; // 'N':옵션그룹만 임시저장, 'Y':옵션그룹 연결된 상품등록완료

    @Column(name = "delete_yn", length = 1)
    private String deleteYn = "N"; // 'N':옵션그룹만 임시저장, 'Y':옵션그룹 연결된 상품등록완료

    @Builder
    public ProductOptionGroup(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            Long pogId, Product product, OptionGroup optionGroup, String activeYn, String deleteYn) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.pogId = pogId;
        this.product = product;
        this.optionGroup = optionGroup;
        this.activeYn = activeYn;
        this.deleteYn = deleteYn;
    }

    // 상품에 연결
    public void assignToProduct(Product product){
        this.product = product;
    }

    // 옵션그룹에 연결
    public void assignToOptionGroup(OptionGroup optionGroup){
        this.optionGroup = optionGroup;
    }

    public void safeDelete() {
        this.deleteYn = "Y";
    }

    public void recovery(){
        this.deleteYn = "N";
    }
}
