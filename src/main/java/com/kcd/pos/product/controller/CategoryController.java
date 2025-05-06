package com.kcd.pos.product.controller;

import com.kcd.pos.product.dto.CategoryRegisterReq;
import com.kcd.pos.product.dto.CategoryRegisterRes;
import com.kcd.pos.product.dto.CategoryReq;
import com.kcd.pos.product.dto.CategoryRes;
import com.kcd.pos.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CategoryController", description = "카테고리 관리 API")
@RestController
@RequestMapping("/pos/category/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final CategoryService categoryService;

    @Operation(summary = "신규 카테고리 등록", description = "신규 카테고리를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "신규 카테고리 생성 성공")
    @PostMapping
    public ResponseEntity<CategoryRegisterRes> registerCategory(@RequestBody @Valid CategoryRegisterReq categoryRegisterReq){
        CategoryRegisterRes response =  service.registerCategory(categoryRegisterReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    @Operation(summary = "카테고리 상세조회", description = "카테고리ID로 카테고리 1건을 상세조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 상세조회 성공")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryRes> getCategoryByCategoryId(@PathVariable Long categoryId){
        CategoryRes response = service.getCategoryByCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "카테고리 목록조회", description = "카테고리명 검색으로 카테고리 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 목록조회 성공")
    @GetMapping
    public ResponseEntity<List<CategoryRes>> getCategoryContainCategoryNm(@RequestParam String categoryNm){
        return ResponseEntity.status(HttpStatus.OK).body(service.getCategoryByCategoryNm(categoryNm));
    }

    @Operation(summary = "카테고리 수정", description = "카테고리ID로 카테고리를 수정합니다.")
    @ApiResponse(responseCode = "204", description = "카테고리 수정 성공")
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryReq request){
        categoryService.updateCategory(categoryId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "카테고리 삭제(safeDelete)", description = "카테고리ID로 조회된 카테고리(속한 상품이 없는)를 삭제합니다.(deleteYn='Y')")
    @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> safeDeleteCategory(@PathVariable Long categoryId) {
        categoryService.safeDeleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
