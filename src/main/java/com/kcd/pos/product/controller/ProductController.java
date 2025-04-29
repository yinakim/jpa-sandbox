package com.kcd.pos.product.controller;

import com.kcd.pos.product.dto.ProductRegisterReq;
import com.kcd.pos.product.dto.ProductRegisterRes;
import com.kcd.pos.product.dto.ProductRes;
import com.kcd.pos.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{productCd}")
    public ResponseEntity<ProductRes> getProductByproductCd(@PathVariable String productCd){
        ProductRes response = service.getProductByproductCd(productCd);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductRes>> getProductContainProductNm(@RequestParam String productNm){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProductByProductNm(productNm));
    }





}
