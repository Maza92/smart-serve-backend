package com.example.demo.controller.inventoryMovement.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.inventoryMovement.IInventoryMovementController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.inventoryMovement.InventoryMovementDto;
import com.example.demo.service.inventoryMovement.IInventoryMovementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory-movements")
@RequiredArgsConstructor
public class InventoryMovementController implements IInventoryMovementController {

    private final IInventoryMovementService inventoryMovementService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<PageDto<InventoryMovementDto>>> getMovementsByItem(
            Integer id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(inventoryMovementService.getMovementsByItem(id, page, size));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<PageDto<InventoryMovementDto>>> getMovementsByOrder(
            Integer id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(inventoryMovementService.getMovementsByOrder(id, page, size));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<PageDto<InventoryMovementDto>>> getMovementsByUser(
            Integer id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(inventoryMovementService.getMovementsByUser(id, page, size));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<PageDto<InventoryMovementDto>>> getMovementsBySupplier(
            Integer id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(inventoryMovementService.getMovementsBySupplier(id, page, size));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<PageDto<InventoryMovementDto>>> getLastMovements(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(inventoryMovementService.getLastMovements(page, size));
    }
}