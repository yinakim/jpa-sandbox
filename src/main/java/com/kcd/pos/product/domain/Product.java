package com.kcd.pos.product.domain;

import com.kcd.pos.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_cd", unique = true, nullable = false, length = 20)
    private String productCd; // 상품 고유 ID - ex. P00001

    @Column(name = "product_nm", nullable = false, length = 100)
    private String productNm;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "store_id", nullable = false, length = 36) // UUID 사용 가정
    private String storeId; // 매장ID - 매장정보 관리 도메인과 연결 시 사용

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 카테고리:상품 = 1:N

    @Builder
    public Product(String productCd, String productNm, int price, String storeId, Category category) {
        this.productCd = productCd;
        this.productNm = productNm;
        this.price = price;
        this.storeId = storeId;
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
