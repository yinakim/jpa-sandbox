package com.kcd.pos.product.controller;

import com.kcd.pos.product.domain.BgColor;
import com.kcd.pos.product.dto.*;
import com.kcd.pos.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/pos/product/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductRegisterRes> registerProduct(@RequestBody ProductRegisterReq registerReq){
        ProductRegisterRes response =  service.registerProduct(registerReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

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
            @RequestParam(required = false) String deleteYn
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
                .build();

        List<ProductRes> results = service.getProducts(condition);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/{productCd}")
    public ResponseEntity<ProductRes> getProductByproductCd(@PathVariable String productCd){
        ProductRes response = service.getProductByproductCd(productCd);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/{productCd}")
    public ResponseEntity<Void> updateProduct(@PathVariable String productCd, @RequestBody ProductUpdateReq request){
        service.updateProduct(productCd, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productCd}")
    public ResponseEntity<Void> safeDeleteProduct(@PathVariable String productCd) {
        service.safeDeleteProduct(productCd);
        return ResponseEntity.noContent().build();
    }
}
