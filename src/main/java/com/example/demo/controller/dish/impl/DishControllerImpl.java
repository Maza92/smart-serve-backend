package com.example.demo.controller.dish.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dish.IDishController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.dish.CreateDishDto;
import com.example.demo.dto.dish.DishDto;
import com.example.demo.dto.dish.DishWithRecipesDto;
import com.example.demo.dto.dish.UpdateDishDto;
import com.example.demo.service.dish.IDishService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/dishes")
@AllArgsConstructor
public class DishControllerImpl implements IDishController {

    private final IDishService dishService;

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> createDish(CreateDishDto request) {
        return ResponseEntity.ok(dishService.createDish(request));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<DishDto>>> getAllDishes(int page, int size, String search,
            String category,
            String isActive, String isFeatured, Double minPrice, Double maxPrice, String sortBy, String sortDirection) {
        return ResponseEntity.ok(dishService.getAllDishes(page, size, search, category, isActive, isFeatured, minPrice,
                maxPrice, sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<DishWithRecipesDto>>> getAllDishesWithRecipeSummary(int page, int size,
            String search,
            String category,
            String isActive, String isFeatured, Double minPrice, Double maxPrice, String sortBy, String sortDirection) {
        return ResponseEntity.ok(dishService.getAllDishesWithRecipeSummary(page, size, search, category, isActive,
                isFeatured, minPrice, maxPrice, sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<DishDto>> getDishById(int id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<DishDto>> updateDish(int id, UpdateDishDto request) {
        return ResponseEntity.ok(dishService.updateDish(id, request));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> deleteDish(int id) {
        return ResponseEntity.ok(dishService.deleteDish(id));
    }
}
