package com.example.demo.service.inventoryItem.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.InventoryItemEntity;
import com.example.demo.repository.InventoryItemRepository;
import com.example.demo.service.inventoryItem.IInventoryItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements IInventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    
    @Override
    public List<InventoryItemEntity> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }
}