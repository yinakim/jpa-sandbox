package com.kcd.pos.product.repository;

import com.kcd.pos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 상품 단건 조회 (상품고유ID)
    Optional<Product> findByProductCd(String productCd);

    // 상품 목록 조회 (키워드 포함검색)
    List<Product> findProductsByProductNmContainsIgnoreCase(String productNm);

    // 카테고리 삭제 전 cascade 체크 : deleteYn=N인 상품 조회
    boolean existsByCategory_CategoryId(Long categoryId);
}
