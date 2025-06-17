package com.example.demo.controller.inventoryItem.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.inventoryItem.IInventoryItemController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.inventoryItem.CreateInventoryItemDto;
import com.example.demo.dto.inventoryItem.InventoryItemDto;
import com.example.demo.dto.inventoryItem.UpdateInventoryItemDto;
import com.example.demo.service.inventoryItem.IInventoryItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory-items")
@RequiredArgsConstructor
public class InventoryItemController implements IInventoryItemController {

    private final IInventoryItemService inventoryItemService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<PageDto<InventoryItemDto>>> getInventoryItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String isActive,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return ResponseEntity.ok(inventoryItemService.getAllInventoryItems(page, size, search, location, isActive,
                sortBy, sortDirection));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<InventoryItemDto>> getInventoryItemById(int id) {
        return ResponseEntity.ok(inventoryItemService.getInventoryItemById(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessDto<InventoryItemDto>> createInventoryItem(
            @RequestBody CreateInventoryItemDto createInventoryItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryItemService.createInventoryItem(createInventoryItemDto));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessDto<InventoryItemDto>> updateInventoryItem(int id,
            @RequestBody UpdateInventoryItemDto updateInventoryItemDto) {
        return ResponseEntity.ok(inventoryItemService.updateInventoryItem(id, updateInventoryItemDto));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessDto<Void>> deleteInventoryItem(int id) {
        return ResponseEntity.ok(inventoryItemService.deleteInventoryItem(id));
    }

}