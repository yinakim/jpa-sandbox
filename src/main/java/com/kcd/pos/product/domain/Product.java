package com.kcd.pos.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // meta 자동생성 (생성자,생성일시/수정자,수정일시)
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", unique = true, nullable = false, length = 20)
    private String productId; // 상품 고유 ID - ex. P00001

    @Column(name = "product_nm", nullable = false, length = 100)
    private String productNm;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 카테고리:상품 = 1:N

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Builder
    public Product(String productId, String productNm, int price, Category category) {
        this.productId = productId;
        this.productNm = productNm;
        this.price = price;
        this.category = category;
    }

    // change 메서드
    public void changeProductNm(String newProductNm){
        this.productNm = newProductNm;
    }

    public void changePrice(int newPrice){
        this.price = newPrice;
    }

    public void changeCategory(Category newCategory){
        this.category = newCategory;
    }
}
