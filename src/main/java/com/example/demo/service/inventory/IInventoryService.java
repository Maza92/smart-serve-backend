package com.example.demo.service.inventory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;
import com.example.demo.dto.inventory.UpdateInventoryItemsStockBatchDto;
import com.example.demo.dto.orderDetail.CreateOrderDetailDto;
import com.example.demo.entity.DishEntity;
import com.example.demo.entity.OrderEntity;

public interface IInventoryService {

    ApiSuccessDto<Void> updateStock(UpdateInventoryItemStockDto request);

    ApiSuccessDto<Void> updateStocksBatch(UpdateInventoryItemsStockBatchDto request);

    CompletableFuture<ApiSuccessDto<Void>> updateStocksBatchAsync(UpdateInventoryItemsStockBatchDto request);

    Boolean isStockAvailable(int itemId, int quantity);

    void checkStockForOrder(List<CreateOrderDetailDto> details, Map<Integer, DishEntity> dishes);

    void updateStockForOrder(OrderEntity order);

}
