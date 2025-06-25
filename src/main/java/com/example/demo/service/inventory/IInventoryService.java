package com.example.demo.service.inventory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;
import com.example.demo.dto.inventory.UpdateInventoryItemsStockBatchDto;

public interface IInventoryService {

    ApiSuccessDto<Void> updateStock(UpdateInventoryItemStockDto request);

    ApiSuccessDto<Void> updateStocksBatch(UpdateInventoryItemsStockBatchDto request);

    CompletableFuture<ApiSuccessDto<Void>> updateStocksBatchAsync(UpdateInventoryItemsStockBatchDto request);
}
