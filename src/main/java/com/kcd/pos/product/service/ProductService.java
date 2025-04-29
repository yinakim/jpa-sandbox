package com.kcd.pos.product.service;

import com.kcd.pos.common.constants.ErrorCode;
import com.kcd.pos.common.exception.SequenceSaveException;
import com.kcd.pos.common.util.JsonUtil;
import com.kcd.pos.product.domain.BgColor;
import com.kcd.pos.product.domain.Category;
import com.kcd.pos.product.domain.Product;
import com.kcd.pos.product.domain.ProductCdSeq;
import com.kcd.pos.product.dto.*;
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
        // 카테고리 변환
        CategoryRes categoryRes = CategoryRes.builder()
                .categoryId(product.getCategory().getCategoryId())
                .categoryNm(product.getCategory().getCategoryNm())
                .storeId(product.getCategory().getStoreId())
                .createdAt(product.getCategory().getCreatedAt())
                .createdBy(product.getCategory().getCreatedBy())
                .modifiedAt(product.getCategory().getModifiedAt())
                .modifiedBy(product.getCategory().getModifiedBy())
                .build();

        return ProductRes.builder()
                .productCd(product.getProductCd())
                .productNm(product.getProductNm())
                .price(product.getPrice())
                .bgColor(product.getBgColor())
                .taxYn(product.getTaxYn())
                .storeId(product.getStoreId())
                .category(categoryRes)
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
                .bgColor(registerReq.getBgColor())
                .taxYn(registerReq.getTaxYn())
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
                .bgColor(savedProduct.getBgColor())
                .taxYn(savedProduct.getTaxYn())
                .storeId(savedProduct.getStoreId())
                .categoryId(savedProduct.getCategory().getCategoryId())
                .categoryNm(savedProduct.getCategory().getCategoryNm())
                .createdAt(savedProduct.getCreatedAt())
                .createdBy(savedProduct.getCreatedBy())
                .modifiedAt(savedProduct.getModifiedAt())
                .modifiedBy(savedProduct.getModifiedBy())
                .build();
    }

    /**
     * 상품고유ID로 상품 단건 조회
     */
    public ProductRes getProductByproductCd(String productCd) {
        Product product = productRepository.findByProductCd(productCd)
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

    /**
     * 상품 수정
     * [수정 시 속성 유효성 체크]
     * 1. 연결된 카테고리 유효성 조회(DELETE_YN='N')
     * 2. 배경색 값 체크
     * 3. 과세 값 확인
     */
    @Transactional
    public void updateProduct(String productCd, ProductUpdateReq request) {
        // target 조회
        Product product = productRepository.findByProductCd(productCd)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productCd));

        // 카테고리 조회
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. ID: " + request.getCategoryId()));

        // 배경색 enum 매핑
        BgColor color;
        try {
            color = BgColor.valueOf(request.getBgColor().name().toUpperCase()); // "blue" → BLUE
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 배경색 값입니다: " + request.getBgColor());
        }

        // 유효한 과세 값 확인 ('Y', 'N')
        String taxYn = request.getTaxYn();
        if (!"Y".equalsIgnoreCase(taxYn) && !"N".equalsIgnoreCase(taxYn)) {
            throw new IllegalArgumentException("부가세는 'Y'(포함) 또는 'N'(미포함)만 입력 가능합니다.");
        }

        /* 수정 가능한 필드
            1. 상품명
            2. 단가
            4. 색상
            5. 과세여부
            3. 카테고리ID (카테고리 변경시)
         */
        // dirtyChecking update
        product.changeProductNm(request.getProductNm());
        product.changePrice(request.getPrice());
        product.changeCategory(category);
        product.changeBgColor(request.getBgColor());
        product.changeTaxYn(request.getTaxYn());
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void safeDeleteProduct(String productCd) {
        Product product = productRepository.findByProductCd(productCd)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productCd));

        // TODO. 상품에 종속된 옵션 함께 삭제 ? vs 상품에 종속된 옵션은 놔두고 상품만 삭제?
        product.safeDeleteProduct();
    }
}
