package com.example.demo.service.order;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.order.CreateDraftOrderDto;
import com.example.demo.dto.order.CreateDraftOrderResponseDto;
import com.example.demo.dto.order.OrderToKitchenDto;
import com.example.demo.dto.order.UpdateOrderWithDetailsDto;
import com.example.demo.dto.order.UpdateOrderWithDetailsResponseDto;
import com.example.demo.entity.OrderEntity;

public interface IOrderService {
    List<OrderEntity> getAllOrders();

    ApiSuccessDto<CreateDraftOrderResponseDto> createDraftOrder(CreateDraftOrderDto order);

    ApiSuccessDto<Void> sendToKitchen(Integer orderId, UpdateOrderWithDetailsDto order);

    ApiSuccessDto<PageDto<OrderToKitchenDto>> getOrdersToKitchen(int page, int size);

    ApiSuccessDto<Void> claimOrderToCook(Integer orderId);

    ApiSuccessDto<Void> readyOrder(Integer orderId);

    OrderEntity sendUpdateMessageToKitchen(OrderEntity order);
}