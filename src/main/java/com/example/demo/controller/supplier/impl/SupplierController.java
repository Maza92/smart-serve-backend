package com.example.demo.controller.supplier.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.supplier.ISupplierController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.supplier.CreateSupplierDto;
import com.example.demo.dto.supplier.SupplierDto;
import com.example.demo.dto.supplier.UpdateSupplierDto;
import com.example.demo.service.supplier.ISupplierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController implements ISupplierController {

    private final ISupplierService supplierService;

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<SupplierDto>>> getAllSuppliers(int page, int size, String search,
            String isActive, String sortBy, String sortDirection) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(page, size, search, isActive, sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<SupplierDto>> getSupplierById(int id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<SupplierDto>> createSupplier(CreateSupplierDto createSupplierDto) {
        return ResponseEntity.status(201).body(supplierService.createSupplier(createSupplierDto));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<SupplierDto>> updateSupplier(int id, UpdateSupplierDto updateSupplierDto) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, updateSupplierDto));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> deleteSupplier(int id) {
        return ResponseEntity.ok(supplierService.deleteSupplier(id));
    }
}