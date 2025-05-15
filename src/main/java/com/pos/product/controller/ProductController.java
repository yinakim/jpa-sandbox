package com.pos.product.controller;

import com.pos.product.domain.BgColor;
import com.pos.product.dto.*;
import com.pos.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Tag(name = "ProductController", description = "상품관리 API")
@RestController
@RequestMapping("/pos/product/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(summary = "신규 상품 등록", description = "신규 상품(상품 별 옵션그룹 및 옵션포함)을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "신규상품 생성 성공")
    @PostMapping
    public ResponseEntity<ProductRegisterRes> registerProduct(@RequestBody ProductRegisterReq registerReq){
        ProductRegisterRes response =  service.registerProduct(registerReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    @Operation(summary = "상품검색", description = "조건에 따라 필터링 된 상품목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<ProductRes>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String productCd,
            @RequestParam(required = false) String productNm,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) BgColor bgColor,
            @RequestParam(required = false) String taxYn,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String deleteYn,
            @RequestParam(required = false) Boolean expand

    ) {
        boolean allNull = Stream.of(productCd, productNm, minPrice, maxPrice, bgColor, taxYn, storeId, categoryId, deleteYn)
                .allMatch(Objects::isNull);
        if (allNull) {
            throw new IllegalArgumentException("최소 1개의 검색 조건은 반드시 필요합니다.");
        }
        ProductReq condition = ProductReq.builder()
                .categoryId(categoryId)
                .productId(productId)
                .productCd(productCd)
                .productNm(productNm)
                .minPrice(Objects.isNull(minPrice)? 0 : minPrice)
                .maxPrice(Objects.isNull(maxPrice)? 0 : maxPrice)
                .bgColor(bgColor)
                .taxYn(taxYn)
                .storeId(storeId)
                .deleteYn(deleteYn)
                .expand(expand)
                .build();

        List<ProductRes> results = service.getProducts(condition);
        return ResponseEntity.ok().body(results);
    }

    @Operation(summary = "상품 상세조회", description = "상품고유번호로 상품(옵션그룹, 옵션 포함) 1건을 상세조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 상세조회 성공")
    @GetMapping("/{productCd}")
    public ResponseEntity<ProductRes> getProductByproductCd(@PathVariable String productCd){
        ProductRes response = service.getProductByproductCd(productCd);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "상품 수정", description = "상품고유번호로 상품데이터를 수정합니다.")
    @ApiResponse(responseCode = "204", description = "상품 수정 성공")
    @PutMapping("/{productCd}")
    public ResponseEntity<Void> updateProduct(@PathVariable String productCd, @RequestBody ProductUpdateReq request){
        service.updateProduct(productCd, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 삭제(safeDelete)", description = "상품고유번호로 상품과 상품에 속한 옵션그룹, 옵션을 삭제합니다.(deleteYn='Y')")
    @ApiResponse(responseCode = "204", description = "상품 삭제 성공")
    @DeleteMapping("/{productCd}")
    public ResponseEntity<Void> safeDeleteProduct(@PathVariable String productCd) {
        service.safeDeleteProduct(productCd);
        return ResponseEntity.noContent().build();
    }
}
