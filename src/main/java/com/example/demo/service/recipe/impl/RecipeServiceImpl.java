package com.example.demo.service.recipe.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.service.recipe.IRecipeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService {

    private final RecipeRepository recipeRepository;
    
    @Override
    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }
}