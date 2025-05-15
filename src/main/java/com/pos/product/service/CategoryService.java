package com.pos.product.service;

import com.pos.product.domain.Category;
import com.pos.product.dto.CategoryRegisterReq;
import com.pos.product.dto.CategoryRegisterRes;
import com.pos.product.dto.CategoryReq;
import com.pos.product.dto.CategoryRes;
import com.pos.product.repository.CategoryRepository;
import com.pos.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final AuditorAware<String> auditor;

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
                .createdAt(result.getCreatedAt())
                .createdBy(result.getCreatedBy())
                .modifiedAt(result.getModifiedAt())
                .modifiedBy(result.getModifiedBy())
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
        return results.stream()
                .map(category -> categoryResMapper(category))
                .collect(Collectors.toList());
    }

    /**
     * 카테고리명 수정
     */
    @Transactional
    public void updateCategory(Long categoryId, CategoryReq request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. ID: " + categoryId));
        category.changeCategoryNm(request.getCategoryNm());
    }

    /**
     * 카테고리 삭제
     * 1. 해당 카테고리에 등록된 상품 존재여부 확인 - 존재하는 경우 삭제불가
     * 2. 존재하지 않는 경우 deleteYn = "Y"
     */
    @Transactional
    public void safeDeleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. ID: " + categoryId));

        if (productRepository.existsByCategory_CategoryId(categoryId)) {
            throw new IllegalStateException("해당 카테고리에 등록된 상품이 존재하여 삭제할 수 없습니다.");
        }
        category.safeDeleteCategory();
    }
}
