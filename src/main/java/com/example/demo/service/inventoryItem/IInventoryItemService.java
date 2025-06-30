package com.example.demo.service.inventoryItem;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.inventoryItem.CreateInventoryItemDto;
import com.example.demo.dto.inventoryItem.InventoryItemDto;
import com.example.demo.dto.inventoryItem.UpdateInventoryItemDto;
import com.example.demo.entity.InventoryItemEntity;

public interface IInventoryItemService {
    ApiSuccessDto<PageDto<InventoryItemDto>> getAllInventoryItems(int page, int size, String search,
            String location, String status, String sortBy, String sortDirection);

    ApiSuccessDto<PageDto<InventoryItemDto>> getAllInventoryItemsBySupplier(int supplierId, int page, int size);

    ApiSuccessDto<InventoryItemDto> getInventoryItemById(int id);

    ApiSuccessDto<InventoryItemDto> createInventoryItem(CreateInventoryItemDto createDto);

    ApiSuccessDto<InventoryItemDto> updateInventoryItem(int id, UpdateInventoryItemDto updateDto);

    List<InventoryItemEntity> getInventoryItemsByDishId(int dishId);

    ApiSuccessDto<Void> deleteInventoryItem(int id);

}