package com.pos.product.service;

import com.pos.common.constants.DataStatus;
import com.pos.common.constants.ErrorCode;
import com.pos.common.exception.SequenceSaveException;
import com.pos.product.domain.*;
import com.pos.product.dto.*;
import com.pos.product.repository.*;
import com.pos.common.util.JsonUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCdSeqRepository productCdSeqRepository;
    private final CategoryRepository categoryRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepsitory optionRepsitory;

    /**
     * CategoryRes - Category 매핑
     */
    private CategoryRes categoryResMapper(Category category) {
        // 카테고리 변환
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
     * ProductRes - Product 매핑, 상품옵션(OptionGroup, Option) 데이터 매핑
     * productCd 값 지정 필수 (Product.id != Product.productCd)
     */
    private ProductRes productResMapperWithOptions(Product product, List<OptionGroupRes> optionGroupResList) {
        CategoryRes categoryRes = categoryResMapper(product.getCategory());
        return ProductRes.builder()
                .productCd(product.getProductCd())
                .productNm(product.getProductNm())
                .price(product.getPrice())
                .bgColor(product.getBgColor())
                .taxYn(product.getTaxYn())
                .storeId(product.getStoreId())
                .category(categoryRes)
                .optionGroupResList(optionGroupResList)
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
        // 1. SEQ 조회 - productCdSeq 조회, 증가
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
            // DataDog 에러로그 시간차 상관없이 발생시각 파악 가능하도록 로깅
            log.error("[상품등록 실패] - PRODUCT_ID_SEQ:{} / 발생시간:{} ", nextSeq, errorTime, e);
            throw new SequenceSaveException(ErrorCode.SEQUENCE_SAVE_FAILED, JsonUtil.toJson(savedProduct));
        }

        // 4. [옵션그룹(+ 옵션포함)] 등록 및 [상품-옵션그룹] 매핑저장
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
                    .activeYn(DataStatus.ACTIVE_Y)
                    .deleteYn(DataStatus.DELETE_N)
                    .requireYn(DataStatus.REQUIRE_Y)
                    .build();
            OptionGroup savedOptionGroup = optionGroupRepository.save(optionGroup); // 응답결과 반환 필요

            // 2. 옵션 생성 + 저장
            List<OptionRegisterRes> optionResList = new ArrayList<>();

            for (OptionRegisterReq optionReq : reqOptGrp.getOptions()) {
                Option option = Option.builder()
                        .optionNm(optionReq.getOptionNm())
                        .extraPrice(optionReq.getExtraPrice())
                        .optionGroup(savedOptionGroup)
                        .activeYn(DataStatus.ACTIVE_Y)
                        .deleteYn(DataStatus.DELETE_N)
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
                    .activeYn(DataStatus.ACTIVE_Y)
                    .deleteYn(DataStatus.DELETE_N)
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
     * 조건 별 상품조회
     */
    public List<ProductRes> getProducts(ProductReq request) {
        List<Product> products = productRepository.findProductsByConditions(
                request.getProductCd(),
                request.getProductNm(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getBgColor(),
                request.getTaxYn(),
                request.getStoreId(),
                request.getCategory(),
                DataStatus.DELETE_N
        );

        // productId 목록을 in 검색하여 매핑
        List<Long> productIds = products.stream()
                .map(Product::getProductId)
                .collect(Collectors.toList());

        // 상품-옵션그룹 매핑조회, 상품ID에 해당되는 옵션그룹 목록 그룹핑
        List<ProductOptionGroup> pogsInProductIds = productOptionGroupRepository.findByProduct_ProductIdInAndDeleteYn(productIds, DataStatus.DELETE_N);
        Map<Long, List<ProductOptionGroup>> groupedByProductId = pogsInProductIds.stream()
                .collect(Collectors.groupingBy(pog -> pog.getProduct().getProductId()));

        return products.stream()
                .map(product -> {
                    // 상품 ID에 해당되는 옵션그룹 목록
                    List<ProductOptionGroup> pogs = groupedByProductId.getOrDefault(product.getProductId(), Collections.emptyList());

                    // 상품 별 옵션그룹 목록 세팅
                    List<OptionGroupRes> optionGroups = buildOptionGroupResList(pogs);
                    return productResMapperWithOptions(product, optionGroups);
                }).collect(Collectors.toList());

    }

    /**
     * 상품 별 옵션그룹, 옵션목록 세팅
     * 상품에 연결된 옵션그룹 + 옵션 조회 (DELETE_N 기준)
     */
    private List<OptionGroupRes> buildOptionGroupResList(List<ProductOptionGroup> pogs) {
        return pogs.stream()
                .map(og -> {
                    OptionGroup optionGroup = og.getOptionGroup();
                    List<OptionRes> options = optionGroup.getOptions()
                            .stream()
                            .filter(opt -> DataStatus.DELETE_N.equals(opt.getDeleteYn())) // 삭제되지 않은 옵션만
                            .map(opt -> OptionRes.builder()
                                    .optionId(opt.getOptionId())
                                    .optionNm(opt.getOptionNm())
                                    .extraPrice(opt.getExtraPrice())
                                    .build())
                            .collect(Collectors.toList());

                    return OptionGroupRes.builder()
                            .options(options)
                            .optionGrpId(optionGroup.getOptionGrpId())
                            .optionGrpNm(optionGroup.getOptionGrpNm())
                            .build();
                }).collect(Collectors.toList());
    }

    /**
     * 상품 별 옵션그룹, 옵션목록 조회
     * 상품에 연결된 옵션그룹 + 옵션 조회 (DELETE_N 기준)
     */
    private List<OptionGroupRes> getOptionGroupsByProductId(Long productId) {
        List<ProductOptionGroup> mappings = productOptionGroupRepository.findByProduct_ProductIdAndDeleteYn(productId, DataStatus.DELETE_N);

        return mappings.stream()
                .map(pog -> {
                    OptionGroup og = pog.getOptionGroup();
                    List<OptionRes> options = og.getOptions().stream()
                            .filter(opt -> DataStatus.DELETE_N.equals(opt.getDeleteYn())) // 삭제되지 않은 옵션만
                            .map(opt -> OptionRes.builder()
                                    .optionId(opt.getOptionId())
                                    .optionNm(opt.getOptionNm())
                                    .extraPrice(opt.getExtraPrice())
                                    .build())
                            .collect(Collectors.toList());

                    return OptionGroupRes.builder()
                            .optionGrpId(og.getOptionGrpId())
                            .optionGrpNm(og.getOptionGrpNm())
                            .options(options)
                            .build();
                }).collect(Collectors.toList());
    }


    /**
     * 상품고유ID로 상품 단건 조회
     */
    public ProductRes getProductByproductCd(String productCd) {
        // 상품 상세 조회
        Product product = productRepository.findByProductCd(productCd)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. 상품고유ID : " + productCd));

        // 상품 옵션 목록 조회
        List<OptionGroupRes> optionGroupsByProductId = getOptionGroupsByProductId(product.getProductId());

        return productResMapperWithOptions(product, optionGroupsByProductId);
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

        product.changeProductNm(request.getProductNm());
        product.changePrice(request.getPrice());
        product.changeCategory(category);
        product.changeBgColor(color);
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
        safeDeleteProductChildren(product.getProductId());
        product.safeDeleteProduct();
    }

    /**
     * 상품 삭제 시, 연관데이터 삭제처리
     * 상품에 종속/매핑된 값의 DELETE_YN = 'Y'
     */
    private void safeDeleteProductChildren(Long productId) {
        List<ProductOptionGroup> targetDatas = productOptionGroupRepository
                .findByProduct_ProductIdAndDeleteYn(productId, DataStatus.DELETE_N); // N -> Y
        for (ProductOptionGroup pog : targetDatas) {
            OptionGroup og = pog.getOptionGroup();
            for (Option opt : og.getOptions()) {
                opt.safeDelete();
            }
            og.safeDelete();
            pog.safeDelete();
        }
    }

}
