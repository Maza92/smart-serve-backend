package com.example.demo.service.inventory;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;

public interface IInventoryService {

    ApiSuccessDto<Void> updateStock(UpdateInventoryItemStockDto request);
}
