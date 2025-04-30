package com.kcd.pos.product.service;

import com.kcd.pos.common.constants.ErrorCode;
import com.kcd.pos.common.exception.SequenceSaveException;
import com.kcd.pos.common.util.JsonUtil;
import com.kcd.pos.product.domain.*;
import com.kcd.pos.product.dto.*;
import com.kcd.pos.product.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    // TODO. 따로추출
    public static final String ACTIVE_Y = "Y";
    public static final String DELETE_Y = "Y";
    public static final String REQUIRE_Y = "Y";

    public static final String ACTIVE_N = "N";
    public static final String DELETE_N = "N";
    public static final String REQUIRE_N = "N";

    private final ProductRepository productRepository;
    private final ProductCdSeqRepository productCdSeqRepository;
    private final CategoryRepository categoryRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepsitory optionRepsitory;

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
     * 2. 카테고리 조회 - 유효성 검사
     * 3. 상품 등록 + productCd SEQ 저장 필수
     * 4. 옵션그룹(+옵션) 등록 / 상품-옵션그룹 매핑 저장 - saveOptionGrpAndMapToProduct 호출
     * 6. 정상등록 결과 반환
     */
    @Transactional
    public ProductRegisterRes registerProduct(ProductRegisterReq registerReq) {
        // 1. SEQ 조회 - productCdSeq 조회, 증가 TODO. lock에 따른 예외처리 추가 필요성 검토
        Long maxSeq = productCdSeqRepository.findMaxSequenceNumberWithLock();
        long nextSeq = Objects.nonNull(maxSeq) ? maxSeq + 1 : 1L;

        // 1-1. productCd 생성 (ex: "P00001")
        String productCd = String.format("P%05d", nextSeq);

        // 2. 카테고리 조회 - 예외 발생 시 400응답(ExceptionHandler)
        Category category = categoryRepository.findById(registerReq.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("[상품등록 실패] 유효하지 않은 카테고리 입니다. 카테고리ID : " + String.valueOf(registerReq.getCategoryId())));

        // 3. 상품 등록
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

        // 3-1. productCd 시퀀스 저장 필수
        try {
            productCdSeqRepository.save(new ProductCdSeq(nextSeq));
        } catch (Exception e){
            LocalDateTime errorTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            // DataDog 에러로그 시간차 상관없이 발생시각 파악 가능하도록 로깅 TODO. DataDog 용 로깅설정 추가하기
            log.error("[상품등록 실패] - PRODUCT_ID_SEQ:{} / 발생시간:{} ", nextSeq, errorTime, e);
            throw new SequenceSaveException(ErrorCode.SEQUENCE_SAVE_FAILED, JsonUtil.toJson(savedProduct));
        }

        // 4. [옵션그룹(+ 옵션포함)] 등록 및 [상품-옵션그룹] 매핑저장
        // TODO. 매핑할 때 엔티티를 넘겨야 해서 한번에 두가지 동작수행(옵션그룹과 옵션 생성, 매핑) 분리하고싶음
        List<OptionGroupRegisterRes> optionGroupRegisterRes = saveOptionGrpAndMapToProduct(registerReq.getOptionGroups(), savedProduct);


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
                .optionGroups(optionGroupRegisterRes) // 옵션 등록결과
                .build();
    }

    /**
     * 신규상품 등록 시 옵션그룹, 옵션 생성
     * 생성된 옵션그룹 목록(옵션목록) 반환 - 주요값만 포함된 DTO사용
     */
    private List<OptionGroupRegisterRes> saveOptionGrpAndMapToProduct(
            List<OptionGroupRegisterReq> requestGroups, Product savedProduct) {
        List<OptionGroupRegisterRes> results = new ArrayList<>();

        for(OptionGroupRegisterReq reqOptGrp : requestGroups){
            // 1. 옵션그룹 생성 + 저장
            OptionGroup optionGroup = OptionGroup.builder()
                    .optionGrpNm(reqOptGrp.getOptionGrpNm())
                    .minSelectCnt(reqOptGrp.getMinSelectCnt())
                    .maxSelectCnt(reqOptGrp.getMaxSelectCnt())
                    .activeYn(ACTIVE_Y)
                    .deleteYn(DELETE_N)
                    .requireYn(REQUIRE_Y)
                    .build();
            OptionGroup savedOptionGroup = optionGroupRepository.save(optionGroup); // 응답결과 반환 필요

            // 2. 옵션 생성 + 저장
            List<OptionRegisterRes> optionResList = new ArrayList<>();

            for (OptionRegisterReq optionReq : reqOptGrp.getOptions()) {
                Option option = Option.builder()
                        .optionNm(optionReq.getOptionNm())
                        .extraPrice(optionReq.getExtraPrice())
                        .optionGroup(savedOptionGroup)
                        .activeYn(ACTIVE_Y)
                        .deleteYn(DELETE_N)
                        .build();

                Option savedOption = optionRepsitory.save(option);

                optionResList.add(OptionRegisterRes.builder()
                                .optionId(savedOption.getOptionId())
                                .optionNm(savedOption.getOptionNm())
                                .extraPrice(savedOption.getExtraPrice())
                                .build()); // Option 결과 list 세팅
            } // Option 목록저장, 결과세팅

            // 3. 상품-옵션그룹 매핑
            ProductOptionGroup pog = ProductOptionGroup.builder()
                    .activeYn(ACTIVE_Y)
                    .deleteYn(DELETE_N)
                    .build();
            pog.assignToProduct(savedProduct);
            pog.assignToOptionGroup(savedOptionGroup);
            productOptionGroupRepository.save(pog);

            // 옵션그룹 결과 (+ 옵션결과 포함) 세팅
            OptionGroupRegisterRes optGrpResponse = OptionGroupRegisterRes.builder()
                    .optionGrpId(savedOptionGroup.getOptionGrpId())
                    .optionGrpNm(savedOptionGroup.getOptionGrpNm())
                    .options(optionResList) // DTO로 세팅
                    .build();

            results.add(optGrpResponse);
        } // OptionGroup 목록저장, 결과세팅
        return results;
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
     * 옵션그룹, 옵션, 매핑 - safeDelete
     */
    @Transactional
    public void safeDeleteProduct(String productCd) {
        Product product = productRepository.findByProductCd(productCd)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productCd));
        //setDeleteYnProductChildren(product.getProductId(), DELETE_Y);
        safeDeleteProductChildren(product.getProductId());
        product.safeDeleteProduct();
    }

    /**
     * 상품 삭제 시, 연관데이터 삭제처리
     * 상품에 종속/매핑된 값의 DELETE_YN = 'Y'
     */
    private void safeDeleteProductChildren(Long productId) {
        List<ProductOptionGroup> targetDatas = productOptionGroupRepository
                .findByProduct_ProductIdAndDeleteYn(productId, DELETE_N); // N -> Y
        for (ProductOptionGroup pog : targetDatas) {
            OptionGroup og = pog.getOptionGroup();
            for (Option opt : og.getOptions()) {
                opt.safeDelete();
            }
            og.safeDelete();
            pog.safeDelete();
        }
    }

    /**
     * 상품 삭제/복구
     * 상품에 종속, 매핑된 값의 DELETE_YN 변경 담당
     */
    private void setDeleteYnProductChildren(Long productId, String deleteYn) {
        // 타겟 데이터 조회를 위한 조건생성
        String targetCondition = deleteYn.equals(DELETE_Y) ? DELETE_N : DELETE_Y;

        // 매핑 테이블에서 productId로 조회, 파라미터로 받은 deleteYn 값과 반대값인 데이터를 조회해온다 (파라미터 값을 적용해야하므로)
        List<ProductOptionGroup> targetDatas = productOptionGroupRepository
                .findByProduct_ProductIdAndDeleteYn(productId, targetCondition);

        if(deleteYn.equals(DELETE_Y)) {
            for (ProductOptionGroup pog : targetDatas) {
                OptionGroup og = pog.getOptionGroup();
                for (Option opt : og.getOptions()) {
                    opt.safeDelete();
                }
                og.safeDelete();
                pog.safeDelete();
            }
        } else {
            for (ProductOptionGroup pog : targetDatas) {
                OptionGroup og = pog.getOptionGroup();
                for (Option opt : og.getOptions()) {
                    opt.recovery();
                }
                og.recovery();
                pog.recovery();
            }
        }
    }

}
