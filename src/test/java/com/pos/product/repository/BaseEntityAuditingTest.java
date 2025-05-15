package com.pos.product.repository;

import com.pos.config.JpaConfig;
import com.pos.product.domain.Category;
import com.pos.common.audit.AuditorAwareImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, AuditorAwareImpl.class}) // JpaConfig Import + AuditorAware 빈 수동 등록
class BaseEntityAuditingTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("BaseEntity Auditing 검증 - 카테고리를 저장 시 BaseEntity 메타데이터가 자동 생성된다.")
    void saveCategory_setsAuditFields() {
        // given : BaseEntity를 상속받는 Category 세팅
        Category category = Category.builder()
                .categoryNm("음료")
                .storeId("sample-store-id-9c12-8fa2b5ef3a72")
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(savedCategory.getCreatedAt()).isNotNull();
        assertThat(savedCategory.getCreatedBy()).isEqualTo("TEMP_USER"); // AuditorAwareImpl에 설정한 임의 currentAuditor 반환
        assertThat(savedCategory.getModifiedAt()).isNotNull();
        assertThat(savedCategory.getModifiedBy()).isEqualTo("TEMP_USER");

        System.out.println("BaseEntity 생성일시: " + savedCategory.getCreatedAt());
        System.out.println("BaseEntity 생성자: " + savedCategory.getCreatedBy());
        System.out.println("BaseEntity 수정일시: " + savedCategory.getModifiedAt());
        System.out.println("BaseEntity 수정자: " + savedCategory.getModifiedBy());
    }
}