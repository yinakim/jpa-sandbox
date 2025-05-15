package com.pos.product.service;

import com.pos.common.constants.TestConstants;
import com.pos.product.domain.Category;
import com.pos.product.dto.CategoryRegisterReq;
import com.pos.product.dto.CategoryRegisterRes;
import com.pos.product.dto.CategoryReq;
import com.pos.product.dto.CategoryRes;
import com.pos.product.repository.CategoryRepository;
import com.pos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryService categoryService;

    private final Long SAMPLE_CATEGORY_ID = 10L;
    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCategory = Category.builder()
                .categoryId(SAMPLE_CATEGORY_ID)
                .categoryNm("수정/삭제 테스트용 카테고리")
                .storeId(TestConstants.SAMPLE_STORE_ID)
                .build();
    }


    @Test
    @DisplayName("신규 카테고리를 등록할 수 있다.")
    void registerCategory_success() {
        // given
        CategoryRegisterReq request = new CategoryRegisterReq("음료", "sample-store-id-9c12-8fa2b5ef3a72"); // 신규 등록 요청

        // Repository save 호출 시 저장된 Category를 반환하도록 세팅
        Category savedCategory = Category.builder()
                .categoryId(1L)
                .categoryNm("음료")
                .storeId("sample-store-id-9c12-8fa2b5ef3a72")
                .build();

        when(categoryRepository.save(any())).thenReturn(savedCategory);

        // when
        CategoryRegisterRes response = categoryService.registerCategory(request);

        // then
        assertThat(response).isNotNull(); // 응답 객체가 null이 아님
        assertThat(response.getCategoryId()).isEqualTo(1L); // 등록된 categoryId가 예상값과 일치
        assertThat(response.getCategoryNm()).isEqualTo("음료"); // 등록된 categoryNm이 예상값과 일치
        assertThat(response.getStoreId()).isEqualTo("sample-store-id-9c12-8fa2b5ef3a72"); // 등록된 storeId가 예상값과 일치
    }

    @Test
    @DisplayName("카테고리 ID로 단건 조회할 수 있다.")
    void getCategoryByCategoryId_success() {
        // given
        Category category = Category.builder()
                .categoryId(1L)
                .categoryNm("디저트")
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category)); // findById 호출시 category 반환

        // when
        CategoryRes result = categoryService.getCategoryByCategoryId(1L);

        // then
        assertThat(result).isNotNull(); // 결과가 null이 아님
        assertThat(result.getCategoryId()).isEqualTo(1L); // categoryId 일치
        assertThat(result.getCategoryNm()).isEqualTo("디저트"); // categoryNm 일치
    }


    @Test
    @DisplayName("존재하지 않는 카테고리 ID로 조회시 예외가 발생한다.")
    void getCategoryByCategoryId_notFound() {
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty()); // 아무것도 찾지 못하는 상황 설정

        // when & then
        assertThatThrownBy(() -> categoryService.getCategoryByCategoryId(1L)) // 메서드 호출시
                .isInstanceOf(IllegalArgumentException.class) // IllegalArgumentException 예외 발생 검증
                .hasMessageContaining("카테고리를 찾을 수 없습니다"); // 예외 메시지 검증
    }


    @Test
    @DisplayName("카테고리명을 포함하는 목록 조회 - 결과 존재")
    void getCategoryByCategoryNm_withResults() {
        // given
        Category category1 = Category.builder().categoryId(1L).categoryNm("커피").build();
        Category category2 = Category.builder().categoryId(2L).categoryNm("콜드브루").build();
        // '커'가 포함된 이름을 가진 카테고리들을 반환하도록 세팅
        when(categoryRepository.findCategoriesByCategoryNmContainsIgnoreCase("커")).thenReturn(Arrays.asList(category1, category2));

        // when
        var result = categoryService.getCategoryByCategoryNm("커");

        // then
        assertThat(result).hasSize(2); // 결과 리스트 크기가 2
        assertThat(result.get(0).getCategoryNm()).contains("커피"); // 첫 번째 결과가 커피 포함
    }

    @Test
    @DisplayName("카테고리명을 포함하는 목록 조회 - 결과 없음")
    void getCategoryByCategoryNm_empty() {
        // given
        when(categoryRepository.findCategoriesByCategoryNmContainsIgnoreCase("없는카테고리"))
                .thenReturn(Collections.emptyList()); // 검색 결과 없음

        // when
        var result = categoryService.getCategoryByCategoryNm("없는카테고리");

        // then
        assertThat(result).isEmpty(); // 결과 리스트가 비어있음
    }

    @Test
    @DisplayName("카테고리명을 수정할 수 있다.")
    void updateCategory_success() {
        // given
        when(categoryRepository.findById(SAMPLE_CATEGORY_ID)).thenReturn(Optional.of(sampleCategory));

        CategoryReq updateReq = CategoryReq.builder()
                .categoryNm("수정된카테고리")
                .build();

        // when
        categoryService.updateCategory(SAMPLE_CATEGORY_ID, updateReq);

        // then
        assertThat(sampleCategory.getCategoryNm()).isEqualTo("수정된카테고리");
    }


}
