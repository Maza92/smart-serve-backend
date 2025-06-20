package com.example.demo.controller.inventory.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.inventory.IInventoryController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;
import com.example.demo.dto.inventory.UpdateInventoryItemsStockBatchDto;
import com.example.demo.service.inventory.IInventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController implements IInventoryController {

    private final IInventoryService inventoryService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<Void>> updateStock(UpdateInventoryItemStockDto request) {
        return ResponseEntity.ok(inventoryService.updateStock(request));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<Void>> updateStocksBatch(UpdateInventoryItemsStockBatchDto request) {
        return ResponseEntity.ok(inventoryService.updateStocksBatch(request));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public CompletableFuture<ResponseEntity<ApiSuccessDto<Void>>> updateStocksBatchAsync(
            UpdateInventoryItemsStockBatchDto request) {
        return CompletableFuture.completedFuture(ResponseEntity.ok(inventoryService.updateStocksBatch(request)));
    }

}