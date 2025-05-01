package com.kcd.pos.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCT_CD_SEQ")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductCdSeq {

    @Id
    @Column(name = "product_seq")
    private Long productSeq; // insert 후 롤백되면 ID가 건너뛸 수 있으므로(ex. P00001 -> P00003) auto increment 사용하지 않고 [수동조작+lock]

    // 신규 상품 등록 시 사용된 시퀀스값 DB 저장 시 사용
    public ProductCdSeq(Long productSeq) {
        this.productSeq = productSeq;
    }
}
