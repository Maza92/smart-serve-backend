package com.example.demo.service.recipe;

import java.util.List;

import com.example.demo.entity.RecipeEntity;

public interface IRecipeService {
    List<RecipeEntity> getAllRecipes();
}