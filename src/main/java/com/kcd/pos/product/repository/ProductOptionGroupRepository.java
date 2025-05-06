package com.kcd.pos.product.repository;

import com.kcd.pos.product.domain.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long> {

    // 상품ID와 deleteYn 값으로 [상품-옵션그룹 매핑데이터] 조회
    List<ProductOptionGroup> findByProduct_ProductIdAndDeleteYn(Long productId, String deleteYn);

    // in절에 해당되는 productOptionGroup 전부 조회
    List<ProductOptionGroup> findByProduct_ProductIdInAndDeleteYn(List<Long> productIds, String deleteYn);
}
