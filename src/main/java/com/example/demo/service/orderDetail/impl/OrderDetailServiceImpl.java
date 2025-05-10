package com.example.demo.service.orderDetail.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderDetailEntity;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.service.orderDetail.IOrderDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    
    @Override
    public List<OrderDetailEntity> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }
}