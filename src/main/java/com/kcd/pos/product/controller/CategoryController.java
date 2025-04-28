package com.kcd.pos.product.controller;

import com.kcd.pos.product.dto.CategoryRegisterReq;
import com.kcd.pos.product.dto.CategoryRegisterRes;
import com.kcd.pos.product.dto.CategoryRes;
import com.kcd.pos.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pos/category/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryRegisterRes> registerCategory(@RequestBody @Valid CategoryRegisterReq categoryRegisterReq){
        CategoryRegisterRes response =  service.registerCategory(categoryRegisterReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    @GetMapping
    @RequestMapping("/{categoryId}")
    public ResponseEntity<CategoryRes> getCategoryByCategoryId(@PathVariable Long categoryId){
        CategoryRes response = service.getCategoryByCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryRes>> getCategoryContainCategoryNm(@RequestParam String categoryNm){
        return ResponseEntity.status(HttpStatus.OK).body(service.getCategoryByCategoryNm(categoryNm));
    }
}
