package com.example.demo.service.inventoryItem;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.inventoryItem.CreateInventoryItemDto;
import com.example.demo.dto.inventoryItem.InventoryItemDto;
import com.example.demo.dto.inventoryItem.UpdateInventoryItemDto;

public interface IInventoryItemService {
    ApiSuccessDto<PageDto<InventoryItemDto>> getAllInventoryItems(int page, int size, String search,
            String location, String status, String sortBy, String sortDirection);

    ApiSuccessDto<PageDto<InventoryItemDto>> getAllInventoryItemsBySupplier(int supplierId, int page, int size);

    ApiSuccessDto<InventoryItemDto> getInventoryItemById(int id);

    ApiSuccessDto<InventoryItemDto> createInventoryItem(CreateInventoryItemDto createDto);

    ApiSuccessDto<InventoryItemDto> updateInventoryItem(int id, UpdateInventoryItemDto updateDto);

    ApiSuccessDto<Void> deleteInventoryItem(int id);

}