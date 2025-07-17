package com.example.demo.service.dish;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.dish.CreateDishDto;
import com.example.demo.dto.dish.DishDto;
import com.example.demo.dto.dish.DishIngredientsDto;
import com.example.demo.dto.dish.DishWithIngredientsDto;
import com.example.demo.dto.dish.DishWithRecipesDto;
import com.example.demo.dto.dish.UpdateDishDto;
import com.example.demo.dto.orderDetail.CreateOrderDetailDto;
import com.example.demo.entity.DishEntity;

public interface IDishService {
        List<DishEntity> getAllDishes();

        ApiSuccessDto<Void> createDish(CreateDishDto request);

        ApiSuccessDto<PageDto<DishDto>> getAllDishes(int page, int size, String search, String category,
                        String isActive, String isFeatured, Double minPrice, Double maxPrice, String sortBy,
                        String sortDirection);

        ApiSuccessDto<PageDto<DishWithRecipesDto>> getAllDishesWithRecipeSummary(int page, int size, String search,
                        String category,
                        String isActive, String isFeatured, Double minPrice, Double maxPrice, String sortBy,
                        String sortDirection);

        ApiSuccessDto<DishDto> getDishById(int id);

        ApiSuccessDto<DishWithIngredientsDto> getDishByIdWithIngredients(int id);

        ApiSuccessDto<DishDto> updateDish(int id, UpdateDishDto request);

        ApiSuccessDto<Void> deleteDish(int id);

        ApiSuccessDto<List<DishIngredientsDto>> getDishIngredients(int id);

        Map<Integer, DishEntity> validateDishesForOrder(List<CreateOrderDetailDto> details);
}