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
