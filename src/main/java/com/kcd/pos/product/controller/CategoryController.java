package com.kcd.pos.product.controller;

import com.kcd.pos.common.util.JsonUtil;
import com.kcd.pos.product.dto.CategoryRegisterReq;
import com.kcd.pos.product.dto.CategoryRegisterRes;
import com.kcd.pos.product.dto.CategoryReq;
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
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryRegisterRes> registerCategory(@RequestBody @Valid CategoryRegisterReq categoryRegisterReq){
        CategoryRegisterRes response =  service.registerCategory(categoryRegisterReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryRes> getCategoryByCategoryId(@PathVariable Long categoryId){
        CategoryRes response = service.getCategoryByCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryRes>> getCategoryContainCategoryNm(@RequestParam String categoryNm){
        return ResponseEntity.status(HttpStatus.OK).body(service.getCategoryByCategoryNm(categoryNm));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryReq request){
        categoryService.updateCategory(categoryId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> safeDeleteCategory(@PathVariable Long categoryId) {
        categoryService.safeDeleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
