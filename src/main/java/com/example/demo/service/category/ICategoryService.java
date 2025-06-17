package com.example.demo.service.category;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.example.demo.dto.category.UpdateCategoryDto;
import com.example.demo.dto.data.ImportResultDto;

public interface ICategoryService {
    ApiSuccessDto<PageDto<CategoryDto>> getAllCategories(int page, int size, String sortBy, String sortDirection);

    ApiSuccessDto<CategoryDto> getCategoryById(int id);

    ApiSuccessDto<CategoryDto> createCategory(CreateCategoryDto category);

    ApiSuccessDto<CategoryDto> updateCategory(int id, UpdateCategoryDto category);

    ApiSuccessDto<Void> deleteCategory(int id);

    ApiSuccessDto<ImportResultDto> importCategories(InputStream inputStream);

    CompletableFuture<ApiSuccessDto<ImportResultDto>> importCategoriesAsync(InputStream inputStream);
}
