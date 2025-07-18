package com.example.demo.service.dish.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.example.demo.dto.dish.DishIngredientsDto;
import com.example.demo.dto.dish.DishWithIngredientsDto;
import com.example.demo.dto.dish.DishWithRecipesDto;
import com.example.demo.dto.dish.UpdateDishDto;
import com.example.demo.dto.orderDetail.CreateOrderDetailDto;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.DishEntity;
import com.example.demo.entity.InventoryItemEntity;
import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.UnitEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IDishMapper;
import com.example.demo.mappers.IInventoryItemMapper;
import com.example.demo.mappers.IRecipeMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.InventoryItemRepository;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.repository.UnitRepository;
import com.example.demo.service.dish.IDishService;
import com.example.demo.service.inventory.IInventoryService;
import com.example.demo.service.inventoryItem.IInventoryItemService;
import com.example.demo.service.recipe.IRecipeService;
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
        private final IInventoryItemService inventoryItemService;
        private final IRecipeService recipeService;
        private final IDishMapper dishMapper;
        private final IInventoryItemMapper inventoryItemMapper;
        private final UnitRepository unitRepository;
        private final IRecipeMapper recipeMapper;
        private final MessageUtils messageUtils;
        private final ApiExceptionFactory apiExceptionFactory;

        @Override
        public List<DishEntity> getAllDishes() {
                return dishRepository.findAll();
        }

        @Override
        public ApiSuccessDto<Void> createDish(CreateDishDto request) {

                List<InventoryItemEntity> inventoryItems = inventoryItemRepository
                                .findAllById(request.getIngredients().stream()
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

                Map<Integer, UnitEntity> unitsMap = unitRepository.findAllByIsBaseUnit(false).stream()
                                .collect(Collectors.toMap(UnitEntity::getId, Function.identity()));

                List<RecipeEntity> recipes = request.getIngredients().stream()
                                .map(ingredient -> {
                                        RecipeEntity entity = recipeMapper.toEntity(ingredient);
                                        entity.setUnit(unitsMap.get(ingredient.getUnitId()));
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
                        String isActive, String isFeatured, Double minPrice, Double maxPrice, String sortBy,
                        String sortDirection) {

                Specification<DishEntity> spec = buildSpecification(search, category, isActive, isFeatured, minPrice,
                                maxPrice);

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
        public ApiSuccessDto<PageDto<DishWithRecipesDto>> getAllDishesWithRecipeSummary(int page, int size,
                        String search,
                        String category, String isActive, String isFeatured, Double minPrice, Double maxPrice,
                        String sortBy,
                        String sortDirection) {

                Specification<DishEntity> spec = buildSpecification(search, category, isActive, isFeatured, minPrice,
                                maxPrice);

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

        public ApiSuccessDto<DishWithIngredientsDto> getDishByIdWithIngredients(int id) {
                DishEntity dish = dishRepository.getDishWithRecipesById(id)
                                .orElseThrow(() -> apiExceptionFactory.entityNotFound("dish.not.found"));

                System.out.println(dish.getRecipes().stream().map(RecipeEntity::getUnit).toList());

                DishWithIngredientsDto dishDto = dishMapper.toDtoWithIngredients(dish);
                dishDto.setIngredients(recipeMapper.toIngredientsDto(dish.getRecipes().stream().toList()));
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
                                        .orElseThrow(() -> apiExceptionFactory
                                                        .entityNotFound("operation.category.not.found"));
                        dish.setCategory(category);
                }

                dishMapper.updateEntityFromDto(request, dish);
                dish = dishRepository.save(dish);

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.dish.update.success"),
                                dishMapper.toDto(dish));
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

        @Override
        public Map<Integer, DishEntity> validateDishesForOrder(List<CreateOrderDetailDto> details) {
                List<Integer> dishIds = details.stream().map(CreateOrderDetailDto::getDishId).toList();
                List<DishEntity> foundDishes = dishRepository.findAllById(dishIds);

                if (foundDishes.size() != dishIds.size()) {
                        throw apiExceptionFactory.badRequestException("operation.dishes.not.found");
                }

                for (DishEntity dish : foundDishes) {
                        if (!dish.getIsActive()) {
                                throw apiExceptionFactory.badRequestException("operation.dish.not.active",
                                                dish.getName());
                        }
                }

                return foundDishes.stream().collect(Collectors.toMap(DishEntity::getId, Function.identity()));
        }

        @Override
        public ApiSuccessDto<List<DishIngredientsDto>> getDishIngredients(int id) {
                List<RecipeEntity> recipes = recipeService.getRecipesByDishId(id);

                if (recipes.size() == 0)
                        throw apiExceptionFactory.entityNotFound("operation.dish.ingredients.not.found");

                List<DishIngredientsDto> ingredientsDto = recipes.stream()
                                .map(recipe -> recipeMapper.toDishIngredientsDto(recipe))
                                .toList();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.dish.get.ingredients.success"),
                                ingredientsDto);
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