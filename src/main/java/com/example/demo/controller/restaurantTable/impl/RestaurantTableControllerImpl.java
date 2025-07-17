package com.example.demo.controller.restaurantTable.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.restaurantTable.IRestaurantTableController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.restaurantTable.CreateRestaurantTableDto;
import com.example.demo.dto.restaurantTable.RestaurantTableDto;
import com.example.demo.dto.restaurantTable.UpdateRestaurantTableDto;
import com.example.demo.service.restaurantTable.IRestaurantTableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurant-tables")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class RestaurantTableControllerImpl implements IRestaurantTableController {

    private final IRestaurantTableService restaurantTableService;

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<RestaurantTableDto>>> getAllRestaurantTables(int page, int size,
            String number,
            String status, String section, String sortBy, String sortDirection) {
        return ResponseEntity.ok(restaurantTableService.getAllRestaurantTables(page, size, number, status, section,
                sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> createRestaurantTable(CreateRestaurantTableDto restaurantTableEntity) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantTableService.createRestaurantTable(restaurantTableEntity));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<RestaurantTableDto>> getRestaurantTable(Integer id) {
        return ResponseEntity.ok(restaurantTableService.getRestaurantTable(id));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> updateRestaurantTable(Integer id,
            UpdateRestaurantTableDto restaurantTableEntity) {
        return ResponseEntity.ok(restaurantTableService.updateRestaurantTable(restaurantTableEntity, id));
    }
}
