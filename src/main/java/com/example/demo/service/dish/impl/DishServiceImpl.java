package com.example.demo.service.dish.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.dish.CreateDishDto;
import com.example.demo.dto.dish.DishDto;
import com.example.demo.dto.dish.DishWithRecipesDto;
import com.example.demo.dto.dish.UpdateDishDto;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.DishEntity;
import com.example.demo.entity.InventoryItemEntity;
import com.example.demo.entity.RecipeEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IDishMapper;
import com.example.demo.mappers.IRecipeMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.InventoryItemRepository;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.service.dish.IDishService;
import com.example.demo.specifications.DishSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements IDishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final IDishMapper dishMapper;
    private final IRecipeMapper recipeMapper;
    private final MessageUtils messageUtils;
    private final ApiExceptionFactory apiExceptionFactory;

    @Override
    public List<DishEntity> getAllDishes() {
        return dishRepository.findAll();
    }

    @Override
    public ApiSuccessDto<Void> createDish(CreateDishDto request) {

        List<InventoryItemEntity> inventoryItems = inventoryItemRepository.findAllById(request.getIngredients().stream()
                .map(ingredient -> ingredient.getInventoryItemId())
                .toList());

        if (inventoryItems.size() != request.getIngredients().size()) {
            throw apiExceptionFactory.entityNotFound("operation.ingredient.not.found");
        }

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.category.not.found"));

        DishEntity dish = dishMapper.toCreateEntity(request);
        dish.setCategory(category);
        dishRepository.save(dish);

        List<RecipeEntity> recipes = request.getIngredients().stream()
                .map(ingredient -> {
                    RecipeEntity entity = recipeMapper.toEntity(ingredient);
                    entity.setDish(dish);
                    return entity;
                })
                .toList();

        recipeRepository.saveAll(recipes);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.dish.create.success"));
    }

    @Override
    public ApiSuccessDto<PageDto<DishDto>> getAllDishes(int page, int size, String search, String category,
            String isActive, String isFeatured, Double minPrice, Double maxPrice, String sortBy, String sortDirection) {

        Specification<DishEntity> spec = buildSpecification(search, category, isActive, isFeatured, minPrice, maxPrice);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "ASC"),
                sortBy != null ? sortBy : "id");

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);
        Page<DishEntity> dishPage = dishRepository.findAll(spec, pageable);

        List<DishDto> dishDtos = dishPage.getContent().stream()
                .map(dishMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.dish.get.all.success"),
                PageDto.fromPage(dishPage, dishDtos));
    }

    @Override
    public ApiSuccessDto<PageDto<DishWithRecipesDto>> getAllDishesWithRecipeSummary(int page, int size, String search,
            String category, String isActive, String isFeatured, Double minPrice, Double maxPrice, String sortBy,
            String sortDirection) {

        Specification<DishEntity> spec = buildSpecification(search, category, isActive, isFeatured, minPrice, maxPrice);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "ASC"),
                sortBy != null ? sortBy : "id");

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);
        Page<DishEntity> dishPage = dishRepository.findAll(spec, pageable);

        PageDto<DishWithRecipesDto> result = dishMapper.toPageDto(dishPage);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.dish.get.all.success"),
                result);
    }

    @Override
    public ApiSuccessDto<DishDto> getDishById(int id) {
        DishEntity dish = dishRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("dish.not.found"));

        DishDto dishDto = dishMapper.toDto(dish);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.dish.get.by.id.success"),
                dishDto);
    }

    @Override
    public ApiSuccessDto<DishDto> updateDish(int id, UpdateDishDto request) {
        DishEntity dish = dishRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("dish.not.found"));

        if (request.getCategoryId() != dish.getCategory().getId()) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.category.not.found"));
            dish.setCategory(category);
        }

        dishMapper.updateEntityFromDto(request, dish);
        dish = dishRepository.save(dish);

        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            recipeRepository.deleteAll(dish.getRecipes());

            for (var ingredient : request.getIngredients()) {
                InventoryItemEntity inventoryItem = inventoryItemRepository.findById(ingredient.getInventoryItemId())
                        .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.ingredient.not.found"));

                RecipeEntity recipe = recipeMapper.toEntity(ingredient);
                recipe.setDish(dish);
                recipe.setInventoryItem(inventoryItem);
                recipeRepository.save(recipe);
            }
        }

        DishDto dishDto = dishMapper.toDto(dish);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.dish.update.success"),
                dishDto);
    }

    @Override
    public ApiSuccessDto<Void> deleteDish(int id) {
        DishEntity dish = dishRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("dish.not.found"));

        recipeRepository.deleteAll(dish.getRecipes());

        dishRepository.delete(dish);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.dish.delete.success"), null);
    }

    private Specification<DishEntity> buildSpecification(String search, String category,
            String isActive, String isFeatured, Double minPrice, Double maxPrice) {
        Specification<DishEntity> spec = Specification.where(null);

        if (search != null && !search.isEmpty()) {
            spec = spec.and(DishSpecifications.nameContains(search)
                    .or(DishSpecifications.descriptionContains(search)));
        }
        if (category != null && !category.isEmpty()) {
            spec = spec.and(DishSpecifications.categoryEquals(category));
        }
        if (isActive != null && !isActive.isEmpty()) {
            spec = spec.and(DishSpecifications.isActiveEquals(isActive));
        }
        if (isFeatured != null && !isFeatured.isEmpty()) {
            spec = spec.and(DishSpecifications.isFeaturedEquals(isFeatured));
        }
        if (minPrice != null) {
            spec = spec.and(DishSpecifications.priceGreaterThanOrEqual(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(DishSpecifications.priceLessThanOrEqual(maxPrice));
        }

        return spec;
    }

}