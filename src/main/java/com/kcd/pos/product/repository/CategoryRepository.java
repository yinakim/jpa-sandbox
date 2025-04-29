package com.kcd.pos.product.repository;

import com.kcd.pos.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 카테고리명 - 단건 조회
    List<Category> findCategoriesByCategoryNmContainsIgnoreCase(String categoryNm);
}
