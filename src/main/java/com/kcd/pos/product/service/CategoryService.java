package com.kcd.pos.product.service;

import com.kcd.pos.product.domain.Category;
import com.kcd.pos.product.dto.CategoryRegisterReq;
import com.kcd.pos.product.dto.CategoryRegisterRes;
import com.kcd.pos.product.dto.CategoryRes;
import com.kcd.pos.product.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * CategoryRes 엔티티 mapper
     */
    private CategoryRes categoryResMapper(Category category){
        return CategoryRes.builder()
                .categoryId(category.getCategoryId())
                .categoryNm(category.getCategoryNm())
                .storeId(category.getStoreId())
                .createdAt(category.getCreatedAt())
                .createdBy(category.getCreatedBy())
                .modifiedAt(category.getModifiedAt())
                .modifiedBy(category.getModifiedBy())
                .build();
    }

    /**
     * 신규 카테고리 등록
     */
    @Transactional
    public CategoryRegisterRes registerCategory(CategoryRegisterReq request) {
        Category category = Category.builder()
                .categoryNm(request.getCategoryNm())
                .storeId(request.getStoreId())
                .build();
        Category result = categoryRepository.save(category);

        return CategoryRegisterRes.builder()
                .categoryId(result.getCategoryId())
                .categoryNm(result.getCategoryNm())
                .storeId(result.getStoreId())
                .build();
    }

    /**
     * 카테고리 단건 조회
     */
    public CategoryRes getCategoryByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. 카테고리ID : " + String.valueOf(categoryId)));
        return categoryResMapper(category);
    }

    /**
     * 카테고리명에 해당되는 카테고리 목록조회
     */
    public List<CategoryRes> getCategoryByCategoryNm(String categoryNm) {
        List<Category> results = categoryRepository.findCategoriesByCategoryNmContainsIgnoreCase(categoryNm);

        if(results.isEmpty()) return Collections.emptyList();
        return results.stream()
                .map(category -> categoryResMapper(category))
                .collect(Collectors.toList());
    }



}
