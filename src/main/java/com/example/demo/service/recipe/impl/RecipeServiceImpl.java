package com.example.demo.service.recipe.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.recipe.CreateRecipeDto;
import com.example.demo.dto.recipe.RecipeDto;
import com.example.demo.dto.recipe.UpdateRecipeDto;
import com.example.demo.entity.RecipeEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IRecipeMapper;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.service.recipe.IRecipeService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService {

    private final RecipeRepository recipeRepository;
    private final IRecipeMapper recipeMapper;
    private final ApiExceptionFactory apiExceptionFactory;
    private final MessageUtils messageUtils;

    @Override
    public ApiSuccessDto<PageDto<RecipeDto>> getAllRecipes(int page, int size, String sortBy, String sortDirection) {
        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

        Page<RecipeEntity> recipes = recipeRepository.findAll(pageable);
        PageDto<RecipeDto> recipesDto = recipeMapper.toPageDto(recipes);
        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.recipe.get.all.success"),
                recipesDto);
    }

    @Override
    public ApiSuccessDto<RecipeDto> getRecipeById(int id) {
        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.recipe.get.by.id.not.found"));

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.recipe.get.by.id.success"),
                recipeMapper.toDto(recipe));
    }

    @Override
    public ApiSuccessDto<RecipeDto> createRecipe(CreateRecipeDto recipe) {
        RecipeEntity recipeEntity = recipeMapper.toCreateEntity(recipe);
        RecipeEntity savedRecipe = recipeRepository.save(recipeEntity);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.recipe.create.success"),
                recipeMapper.toDto(savedRecipe));
    }

    @Override
    public ApiSuccessDto<RecipeDto> updateRecipe(int id, UpdateRecipeDto recipe) {
        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.recipe.update.not.found"));

        recipeMapper.updateEntityFromDto(recipe, recipeEntity);
        RecipeEntity savedRecipe = recipeRepository.save(recipeEntity);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.recipe.update.success"),
                recipeMapper.toDto(savedRecipe));
    }

    @Override
    public ApiSuccessDto<Void> deleteRecipe(int id) {
        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.recipe.delete.not.found"));

        recipeRepository.delete(recipe);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.recipe.delete.success"));
    }
}