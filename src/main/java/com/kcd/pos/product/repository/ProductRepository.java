package com.kcd.pos.product.repository;

import com.kcd.pos.product.domain.BgColor;
import com.kcd.pos.product.domain.Category;
import com.kcd.pos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 상품 단건 조회 (상품고유ID)
    Optional<Product> findByProductCd(String productCd);

    // 카테고리 삭제 전 cascade 체크 : deleteYn=N인 상품 조회
    boolean existsByCategory_CategoryId(Long categoryId);

    @Query("""
                SELECT p FROM Product p
                WHERE (:productCd IS NULL OR p.productCd = :productCd)
                  AND (:productNm IS NULL OR LOWER(p.productNm) LIKE LOWER(CONCAT('%', :productNm, '%')))
                  AND (:minPrice = 0 OR p.price >= :minPrice)
                  AND (:maxPrice = 0 OR p.price <= :maxPrice)
                  AND (:bgColor IS NULL OR p.bgColor = :bgColor)
                  AND (:taxYn IS NULL OR p.taxYn = :taxYn)
                  AND (:storeId IS NULL OR p.storeId = :storeId)
                  AND (:category IS NULL OR p.category = :category)
                  AND (:deleteYn IS NULL OR p.deleteYn = :deleteYn)
            """)
    List<Product> findProductsByConditions(@Param("productCd") String productCd,
                                           @Param("productNm") String productNm,
                                           @Param("minPrice") Integer minPrice,
                                           @Param("maxPrice") Integer maxPrice,
                                           @Param("bgColor") BgColor bgColor,
                                           @Param("taxYn") String taxYn,
                                           @Param("storeId") String storeId,
                                           @Param("category") Category category,
                                           @Param("deleteYn") String deleteYn);
}
