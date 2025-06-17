package com.example.demo.controller.category;

import java.io.InputStream;

import org.apache.catalina.connector.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.example.demo.dto.category.UpdateCategoryDto;
import com.example.demo.dto.data.ImportResultDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@Tag(name = "Category", description = "Operations with categories")
public interface ICategoryController {
        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get all categories", description = "Get all categories")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping
        @Parameter(name = "page", description = "Page number", example = "1")
        @Parameter(name = "size", description = "Page size", example = "10")
        @Parameter(name = "sortBy", description = "Sort by field", example = "id")
        @Parameter(name = "sortDirection", description = "Sort direction", example = "ASC")
        ResponseEntity<ApiSuccessDto<PageDto<CategoryDto>>> getAllCategories(
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "10") int size,
                        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                        @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection);
        
        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get all categories by type", description = "Get all categories by type")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping("/type")
        @Parameter(name = "page", description = "Page number", example = "1")
        @Parameter(name = "size", description = "Page size", example = "10")
        @Parameter(name = "sortBy", description = "Sort by field", example = "id")
        @Parameter(name = "sortDirection", description = "Sort direction", example = "ASC")
        @Parameter(name = "categoryType", description = "Category type", example = "DISH")
        ResponseEntity<ApiSuccessDto<PageDto<CategoryDto>>> getAllCategoriesByType(
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "10") int size,
                        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                        @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection,
                        @RequestParam(required = false) String categoryType);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get category by id", description = "Get category by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping("/{id}")
        ResponseEntity<ApiSuccessDto<CategoryDto>> getCategoryById(
                        @Parameter(description = "Category id", required = true, example = "1") @PathVariable(name = "id") Integer id);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Create category", description = "Create category")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @PostMapping
        ResponseEntity<ApiSuccessDto<CategoryDto>> createCategory(
                        @Parameter(description = "Category data", required = true) @Valid @RequestBody CreateCategoryDto category);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Update category", description = "Update category")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @PutMapping("/{id}")
        ResponseEntity<ApiSuccessDto<CategoryDto>> updateCategory(
                        @Parameter(description = "Category id", required = true, example = "1") @PathVariable(name = "id") Integer id,
                        @Parameter(description = "Category data", required = true) @Valid @RequestBody UpdateCategoryDto category);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Delete category", description = "Delete category")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @DeleteMapping("/{id}")
        ResponseEntity<ApiSuccessDto<Void>> deleteCategory(
                        @Parameter(description = "Category id", required = true, example = "1") @PathVariable(name = "id") Integer id);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Import categories", description = "Import categories from an Excel file")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        ResponseEntity<ApiSuccessDto<ImportResultDto>> importCategories(
                        @Parameter(description = "Categories data", required = true) @Valid @RequestPart(name = "file") MultipartFile file);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Import categories asynchronously", description = "Import categories asynchronously")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @PostMapping(value = "/import-async", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        ResponseEntity<ApiSuccessDto<ImportResultDto>> importCategoriesAsync(
                        @Parameter(description = "Categories data", required = true) @Valid @RequestPart(name = "file") MultipartFile file);
}
