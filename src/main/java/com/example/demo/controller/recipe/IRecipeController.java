package com.example.demo.controller.recipe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.recipe.CreateRecipeDto;
import com.example.demo.dto.recipe.RecipeDto;
import com.example.demo.dto.recipe.UpdateRecipeDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@Tag(name = "Recipe", description = "Operations with recipes")
public interface IRecipeController {
        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get all recipes", description = "Get all recipes")
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
        @Parameter(name = "sortDirection", description = "Sort direction", example = "asc")
        ResponseEntity<ApiSuccessDto<PageDto<RecipeDto>>> getAllRecipes(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDirection);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get recipe by ID", description = "Get recipe by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping("/{id}")
        ResponseEntity<ApiSuccessDto<RecipeDto>> getRecipeById(@PathVariable Integer id);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Create recipe", description = "Create a new recipe")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @PostMapping
        ResponseEntity<ApiSuccessDto<RecipeDto>> createRecipe(@Valid @RequestBody CreateRecipeDto recipe);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Update recipe", description = "Update an existing recipe")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @PutMapping("/{id}")
        ResponseEntity<ApiSuccessDto<RecipeDto>> updateRecipe(@PathVariable Integer id,
                        @Valid @RequestBody UpdateRecipeDto recipe);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Delete recipe", description = "Delete an existing recipe")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @DeleteMapping("/{id}")
        ResponseEntity<ApiSuccessDto<Void>> deleteRecipe(@PathVariable Integer id);
}