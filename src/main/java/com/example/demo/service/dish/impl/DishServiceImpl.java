package com.example.demo.service.dish.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DishEntity;
import com.example.demo.repository.DishRepository;
import com.example.demo.service.dish.IDishService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements IDishService {

    private final DishRepository dishRepository;
    
    @Override
    public List<DishEntity> getAllDishes() {
        return dishRepository.findAll();
    }
}