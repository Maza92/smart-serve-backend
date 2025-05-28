package com.example.demo.service.supplier;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.supplier.CreateSupplierDto;
import com.example.demo.dto.supplier.SupplierDto;
import com.example.demo.dto.supplier.UpdateSupplierDto;

public interface ISupplierService {
    ApiSuccessDto<PageDto<SupplierDto>> getAllSuppliers(int page, int size, String search,
            String sortBy, String sortDirection);

    ApiSuccessDto<SupplierDto> getSupplierById(int id);

    ApiSuccessDto<SupplierDto> createSupplier(CreateSupplierDto createDto);

    ApiSuccessDto<SupplierDto> updateSupplier(int id, UpdateSupplierDto updateDto);

    ApiSuccessDto<Void> deleteSupplier(int id);
}