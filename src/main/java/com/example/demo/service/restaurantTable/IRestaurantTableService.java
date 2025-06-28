package com.example.demo.service.restaurantTable;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.restaurantTable.CreateRestaurantTableDto;
import com.example.demo.dto.restaurantTable.RestaurantTableDto;
import com.example.demo.dto.restaurantTable.UpdateRestaurantTableDto;
import com.example.demo.entity.RestaurantTableEntity;
import com.example.demo.enums.RestaurantTableEnum;

public interface IRestaurantTableService {
    List<RestaurantTableEntity> getAllRestaurantTables();

    ApiSuccessDto<PageDto<RestaurantTableDto>> getAllRestaurantTables(int page, int size, String number, String status,
            String section, String sortBy, String sortDirection);

    ApiSuccessDto<Void> createRestaurantTable(CreateRestaurantTableDto restaurantTableEntity);

    ApiSuccessDto<RestaurantTableDto> getRestaurantTable(Integer id);

    ApiSuccessDto<Void> updateRestaurantTable(UpdateRestaurantTableDto restaurantTableEntity, Integer id);

    RestaurantTableEntity isRestaurantTableAvailable(Integer id);

    RestaurantTableEntity setStatus(Integer id, RestaurantTableEnum status);

    RestaurantTableEntity setStatus(RestaurantTableEntity restaurantTable, RestaurantTableEnum status);

    RestaurantTableEntity setStatusAndSendMessage(Integer id, RestaurantTableEnum status);

    RestaurantTableEntity setStatusAndSendMessage(RestaurantTableEntity restaurantTable, RestaurantTableEnum status);
}