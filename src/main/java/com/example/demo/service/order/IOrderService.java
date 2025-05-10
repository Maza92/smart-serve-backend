package com.example.demo.service.order;

import java.util.List;

import com.example.demo.entity.OrderEntity;

public interface IOrderService {
    List<OrderEntity> getAllOrders();
}