package com.example.demo.controller.orden.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.orden.IOrdenController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.order.CreateDraftOrderDto;
import com.example.demo.dto.order.CreateDraftOrderResponseDto;
import com.example.demo.dto.order.OrderToKitchenDto;
import com.example.demo.dto.order.UpdateOrderWithDetailsDto;
import com.example.demo.dto.order.UpdateOrderWithDetailsResponseDto;
import com.example.demo.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class OrdenController implements IOrdenController {

    private final IOrderService orderService;

    @Override
    public ResponseEntity<ApiSuccessDto<CreateDraftOrderResponseDto>> createDraftOrder(
            CreateDraftOrderDto request) {
        return ResponseEntity.ok(orderService.createDraftOrder(request));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> sendToKitchen(
            Integer orderId, UpdateOrderWithDetailsDto request) {
        return ResponseEntity.ok(orderService.sendToKitchen(orderId, request));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> claimOrderToCook(Integer orderId) {
        return ResponseEntity.ok(orderService.claimOrderToCook(orderId));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> readyOrder(Integer orderId) {
        return ResponseEntity.ok(orderService.readyOrder(orderId));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<OrderToKitchenDto>>> getOrdersToKitchen(
            int page, int size) {
        return ResponseEntity.ok(orderService.getOrdersToKitchen(page, size));
    }
}