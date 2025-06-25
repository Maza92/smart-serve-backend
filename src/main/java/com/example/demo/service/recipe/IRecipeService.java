package com.example.demo.service.recipe;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.recipe.CreateRecipeDto;
import com.example.demo.dto.recipe.RecipeDto;
import com.example.demo.dto.recipe.UpdateRecipeDto;

public interface IRecipeService {
    ApiSuccessDto<PageDto<RecipeDto>> getAllRecipes(int page, int size, String sortBy, String sortDirection);

    ApiSuccessDto<RecipeDto> getRecipeById(int id);

    ApiSuccessDto<RecipeDto> createRecipe(CreateRecipeDto recipe);

    ApiSuccessDto<RecipeDto> updateRecipe(int id, UpdateRecipeDto recipe);

    ApiSuccessDto<Void> deleteRecipe(int id);
}