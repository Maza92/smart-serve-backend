package com.example.demo.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderEntity;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    
    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}