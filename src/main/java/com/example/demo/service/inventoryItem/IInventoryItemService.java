package com.example.demo.service.inventoryItem;

import java.util.List;

import com.example.demo.entity.InventoryItemEntity;

public interface IInventoryItemService {
    List<InventoryItemEntity> getAllInventoryItems();
}