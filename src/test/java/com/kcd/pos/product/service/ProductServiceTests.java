package com.kcd.pos.product.service;

import com.kcd.pos.product.domain.BgColor;
import com.kcd.pos.product.domain.Category;
import com.kcd.pos.product.domain.Product;
import com.kcd.pos.product.domain.ProductCdSeq;
import com.kcd.pos.product.dto.ProductRegisterReq;
import com.kcd.pos.product.dto.ProductRegisterRes;
import com.kcd.pos.product.repository.CategoryRepository;
import com.kcd.pos.product.repository.ProductCdSeqRepository;
import com.kcd.pos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.kcd.pos.common.constants.TestConstants.SAMPLE_STORE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCdSeqRepository productCdSeqRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        initTestCategory(); // 상품 관련 테스트에 필요한 기본 카테고리 세팅
    }

    private Category testCategory1;
    private Category testCategory2;
    private void initTestCategory(){
        testCategory1 = Category.builder()
                .categoryId(1L)
                .categoryNm("음료")
                .storeId(SAMPLE_STORE_ID)
                .build();
        testCategory2 = Category.builder()
                .categoryId(2L)
                .categoryNm("타르트")
                .storeId(SAMPLE_STORE_ID)
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(testCategory2));
    }


    @Test
    @DisplayName("신규 상품 등록에 성공한다.")
    void registerProduct_success(){
        Long categoryId = testCategory1.getCategoryId();

        // given
        ProductRegisterReq request
                = ProductRegisterReq.builder()
                .productNm("아메리카노")
                .price(3000)
                .bgColor(BgColor.BLUE)
                .taxYn("Y")
                .storeId(SAMPLE_STORE_ID)
                .categoryId(categoryId)
                .build();

        when(productCdSeqRepository.findMaxSequenceNumberWithLock()).thenReturn(5L); // 현재 최대 시퀀스는 5
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(testCategory1));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productCdSeqRepository.save(any(ProductCdSeq.class))).thenReturn(new ProductCdSeq(6L)); // nextSeq 저장 성공

        // when
        ProductRegisterRes response = productService.registerProduct(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getProductCd()).isEqualTo("P00006"); // "P" + (max시퀀스+1)
        assertThat(response.getProductNm()).isEqualTo("아메리카노");
        assertThat(response.getPrice()).isEqualTo(3000);
        assertThat(response.getStoreId()).isEqualTo(SAMPLE_STORE_ID);
        assertThat(response.getCategoryId()).isEqualTo(categoryId);
    }

}
