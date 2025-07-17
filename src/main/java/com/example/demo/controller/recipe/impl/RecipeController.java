package com.example.demo.controller.recipe.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.recipe.IRecipeController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.recipe.CreateRecipeDto;
import com.example.demo.dto.recipe.RecipeDto;
import com.example.demo.dto.recipe.UpdateRecipeDto;
import com.example.demo.service.recipe.IRecipeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RecipeController implements IRecipeController {
    private final IRecipeService recipeService;

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<RecipeDto>>> getAllRecipes(int page, int size, String sortBy,
            String sortDirection) {
        return ResponseEntity.ok(recipeService.getAllRecipes(page, size, sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<RecipeDto>> getRecipeById(Integer id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<RecipeDto>> createRecipe(CreateRecipeDto recipe) {
        return ResponseEntity.ok(recipeService.createRecipe(recipe));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<RecipeDto>> updateRecipe(Integer id, UpdateRecipeDto recipe) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipe));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> deleteRecipe(Integer id) {
        return ResponseEntity.ok(recipeService.deleteRecipe(id));
    }
}