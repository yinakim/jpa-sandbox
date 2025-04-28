package com.kcd.pos.product.domain;

import com.kcd.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "categoryNm", nullable = false, length = 50)
    private String categoryNm;

    @Column(name = "store_id", nullable = false, length = 36) // UUID 사용 가정
    private String storeId; // 매장ID - 매장정보 관리 도메인과 연결 시 사용

    @Builder
    public Category(
            String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt,
            Long categoryId, String categoryNm, String storeId) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.categoryId = categoryId;
        this.categoryNm = categoryNm;
        this.storeId = storeId;
    }
}
