package com.example.demo.service.supplier.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.supplier.CreateSupplierDto;
import com.example.demo.dto.supplier.SupplierDto;
import com.example.demo.dto.supplier.UpdateSupplierDto;
import com.example.demo.entity.SupplierEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.ISupplierMapper;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.service.supplier.ISupplierService;
import com.example.demo.specifications.SupplierSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements ISupplierService {

        private final SupplierRepository supplierRepository;
        private final ISupplierMapper supplierMapper;
        private final MessageUtils messageUtils;
        private final ApiExceptionFactory apiExceptionFactory;

        @Override
        public ApiSuccessDto<PageDto<SupplierDto>> getAllSuppliers(int page, int size, String search,
                        String isActive, String sortBy, String sortDirection) {

                if (size <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

                if (page <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

                Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
                Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

                Specification<SupplierEntity> spec = Specification.where(null);

                if (search != null && !search.isBlank()) {
                        spec = spec.or(SupplierSpecifications.nameContains(search))
                                        .or(SupplierSpecifications.contactPersonContains(search))
                                        .or(SupplierSpecifications.emailContains(search));
                }

                if (isActive != null && !isActive.isBlank()) {
                        spec = spec.and(SupplierSpecifications.isActiveEquals(isActive));
                }

                Page<SupplierEntity> suppliers = supplierRepository.findAll(spec, pageable);

                List<SupplierDto> suppliersDto = suppliers.getContent().stream()
                                .map(supplierMapper::toDto)
                                .toList();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.supplier.get.all.success"),
                                PageDto.fromPage(suppliers, suppliersDto));
        }

        @Override
        public ApiSuccessDto<SupplierDto> getSupplierById(int id) {
                SupplierEntity supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> apiExceptionFactory
                                                .entityNotFound("operation.supplier.get.by.id.not.found"));

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.supplier.get.by.id.success"),
                                supplierMapper.toDto(supplier));
        }

        @Override
        public ApiSuccessDto<SupplierDto> createSupplier(CreateSupplierDto createDto) {
                SupplierEntity supplier = supplierMapper.toEntity(createDto);
                SupplierEntity savedSupplier = supplierRepository.save(supplier);

                return ApiSuccessDto.of(HttpStatus.CREATED.value(),
                                messageUtils.getMessage("operation.supplier.create.success"),
                                supplierMapper.toDto(savedSupplier));
        }

        @Override
        public ApiSuccessDto<SupplierDto> updateSupplier(int id, UpdateSupplierDto updateDto) {
                SupplierEntity supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> apiExceptionFactory
                                                .entityNotFound("operation.supplier.update.not.found"));

                supplierMapper.updateEntityFromDto(updateDto, supplier);
                SupplierEntity savedSupplier = supplierRepository.save(supplier);

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.supplier.update.success"),
                                supplierMapper.toDto(savedSupplier));
        }

        @Override
        public ApiSuccessDto<Void> deleteSupplier(int id) {
                SupplierEntity supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> apiExceptionFactory
                                                .entityNotFound("operation.supplier.delete.not.found"));

                supplierRepository.delete(supplier);

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.supplier.delete.success"), null);
        }
}