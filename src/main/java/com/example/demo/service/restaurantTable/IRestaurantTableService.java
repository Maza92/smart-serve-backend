package com.example.demo.service.restaurantTable;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.restaurantTable.CreateRestaurantTableDto;
import com.example.demo.dto.restaurantTable.RestaurantTableDto;
import com.example.demo.dto.restaurantTable.UpdateRestaurantTableDto;
import com.example.demo.entity.RestaurantTableEntity;

public interface IRestaurantTableService {
    List<RestaurantTableEntity> getAllRestaurantTables();

    ApiSuccessDto<PageDto<RestaurantTableDto>> getAllRestaurantTables(int page, int size);

    ApiSuccessDto<Void> createRestaurantTable(CreateRestaurantTableDto restaurantTableEntity);

    ApiSuccessDto<RestaurantTableDto> getRestaurantTable(Integer id);

    ApiSuccessDto<Void> updateRestaurantTable(UpdateRestaurantTableDto restaurantTableEntity, Integer id);
}