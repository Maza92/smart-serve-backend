package com.example.demo.service.supplier.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.SupplierEntity;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.service.supplier.ISupplierService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements ISupplierService {

    private final SupplierRepository supplierRepository;
    
    @Override
    public List<SupplierEntity> getAllSuppliers() {
        return supplierRepository.findAll();
    }
}