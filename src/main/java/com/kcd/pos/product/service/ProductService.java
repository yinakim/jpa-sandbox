package com.kcd.pos.product.service;

import com.kcd.pos.common.exception.ErrorCode;
import com.kcd.pos.common.exception.SequenceSaveException;
import com.kcd.pos.common.util.JsonUtil;
import com.kcd.pos.product.domain.Category;
import com.kcd.pos.product.domain.Product;
import com.kcd.pos.product.domain.ProductCdSeq;
import com.kcd.pos.product.dto.ProductRegisterReq;
import com.kcd.pos.product.dto.ProductRegisterRes;
import com.kcd.pos.product.dto.ProductRes;
import com.kcd.pos.product.repository.CategoryRepository;
import com.kcd.pos.product.repository.ProductCdSeqRepository;
import com.kcd.pos.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCdSeqRepository productCdSeqRepository;
    private final CategoryRepository categoryRepository;

    /**
     * ProductRes - Product 매핑
     * productCd 값 지정 필수 (Product.id != Product.productCd)
     */
    private ProductRes productResMapper(Product product) {
        return ProductRes.builder()
                .productCd(product.getProductCd())
                .productNm(product.getProductNm())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .createdBy(product.getCreatedBy())
                .modifiedAt(product.getModifiedAt())
                .modifiedBy(product.getModifiedBy())
                .build();
    }

    /**
     * 신규상품 등록
     * 1. 상품 고유ID 생성(DB SEQ 사용)
     * 2. 상품 카테고리 유효성 검사
     * 3. 상품 저장 + DB SEQ 저장
     * 4. 정상등록 결과 반환
     */
    @Transactional
    public ProductRegisterRes registerProduct(ProductRegisterReq registerReq) {
        // 1. SEQ 조회 - productCdSeq 조회, 증가 TODO. lock에 따른 예외처리 추가 필요성 검토
        Long maxSeq = productCdSeqRepository.findMaxSequenceNumberWithLock();
        long nextSeq = Objects.nonNull(maxSeq) ? maxSeq + 1 : 1L;

        // 2. productCd 생성 (ex: "P00001")
        String productCd = String.format("P%05d", nextSeq);
        System.out.println(" 신규 productCd ................. : " + productCd);

        // 3. 카테고리 조회 - 예외 발생 시 400응답(ExceptionHandler)
        Category category = categoryRepository.findById(registerReq.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("[상품등록 실패] 유효하지 않은 카테고리 입니다. 카테고리ID : " + String.valueOf(registerReq.getCategoryId())));

        // 4. 상품 등록
        Product newProduct = Product.builder()
                .productCd(productCd) // ex.P00001
                .productNm(registerReq.getProductNm())
                .price(registerReq.getPrice())
                .storeId(registerReq.getStoreId())
                .category(category)
                .build();
        Product savedProduct = productRepository.save(newProduct);

        // 5. productCd 시퀀스 저장 필수
        try {
            productCdSeqRepository.save(new ProductCdSeq(nextSeq));
        } catch (Exception e){
            LocalDateTime errorTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            // DataDog 에러로그 시간차 상관없이 발생시각 파악 가능하도록 로깅 TODO. DataDog 용 로깅설정 추가하기
            log.error("[상품등록 실패] - PRODUCT_ID_SEQ:{} / 발생시간:{} ", nextSeq, errorTime, e);
            throw new SequenceSaveException(ErrorCode.SEQUENCE_SAVE_FAILED, JsonUtil.toJson(savedProduct));
        }

        // 6. 정상등록 결과반환
        return ProductRegisterRes.builder()
                .productCd(savedProduct.getProductCd())
                .productNm(savedProduct.getProductNm())
                .price(savedProduct.getPrice())
                .storeId(savedProduct.getStoreId())
                .categoryId(savedProduct.getCategory().getCategoryId())
                .categoryNm(savedProduct.getCategory().getCategoryNm())
                .build();
    }

    /**
     * 상품고유ID로 상품 단건 조회
     */
    public ProductRes getProductByproductCd(String productCd) {
        Product product = productRepository.findByproductCd(productCd)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. 상품고유ID : " + productCd));
        ProductRes productRes = productResMapper(product);
        return productRes;
    }

    /**
     * 상품 목록 조회
     * TODO. price기준 조회 추가 - ex.일정금액 이상/이하인 상품목록 조회
     * TODO. 카테고리 별 상품 목록 조회
     */
    public List<ProductRes> getProductByProductNm(String productNm) {
        List<Product> results = productRepository.findProductsByProductNmContainsIgnoreCase(productNm);

        if(results.isEmpty()) return Collections.emptyList();
        return results.stream()
                .map(product -> productResMapper(product))
                .collect(Collectors.toList());
    }
}
