package com.example.demo.service.restaurantTable.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.RestaurantTableEntity;
import com.example.demo.repository.RestaurantTableRepository;
import com.example.demo.service.restaurantTable.IRestaurantTableService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements IRestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    
    @Override
    public List<RestaurantTableEntity> getAllRestaurantTables() {
        return restaurantTableRepository.findAll();
    }
}