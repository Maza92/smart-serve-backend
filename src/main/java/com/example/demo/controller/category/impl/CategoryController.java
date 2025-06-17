package com.example.demo.controller.category.impl;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.controller.category.ICategoryController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.example.demo.dto.category.UpdateCategoryDto;
import com.example.demo.dto.data.ImportResultDto;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController implements ICategoryController {
    private final ICategoryService categoryService;
    private final ApiExceptionFactory apiExceptionFactory;

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<CategoryDto>>> getAllCategories(int page, int size, String sortBy,
            String sortDirection) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size, sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<CategoryDto>> getCategoryById(Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<CategoryDto>> createCategory(CreateCategoryDto category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<CategoryDto>> updateCategory(Integer id, UpdateCategoryDto category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> deleteCategory(Integer id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<ImportResultDto>> importCategories(MultipartFile file) {
        try {
            return ResponseEntity.ok(categoryService.importCategories(file.getInputStream()));
        } catch (IOException e) {
            throw apiExceptionFactory.badRequestException("operation.category.import.invalid.file");
        }
    }

    @Override
    public ResponseEntity<ApiSuccessDto<ImportResultDto>> importCategoriesAsync(MultipartFile file) {
        try {
            return ResponseEntity.ok(categoryService.importCategoriesAsync(file.getInputStream()).get());
        } catch (Exception e) {
            throw apiExceptionFactory.badRequestException("operation.category.import.invalid.file");
        }
    }
}
