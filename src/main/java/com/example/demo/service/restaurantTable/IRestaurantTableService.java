package com.example.demo.service.restaurantTable;

import java.util.List;

import com.example.demo.entity.RestaurantTableEntity;

public interface IRestaurantTableService {
    List<RestaurantTableEntity> getAllRestaurantTables();
}