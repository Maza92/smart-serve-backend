package com.example.demo.service.inventoryItem.impl;

import java.math.BigDecimal;
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
import com.example.demo.dto.inventoryItem.CreateInventoryItemDto;
import com.example.demo.dto.inventoryItem.InventoryItemDto;
import com.example.demo.dto.inventoryItem.UpdateInventoryItemDto;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.InventoryItemEntity;
import com.example.demo.entity.SupplierEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IInventoryItemMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.InventoryItemRepository;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.service.inventoryItem.IInventoryItemService;
import com.example.demo.specifications.InventoryItemSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements IInventoryItemService {

        private final InventoryItemRepository inventoryItemRepository;
        private final SupplierRepository supplierRepository;
        private final CategoryRepository categoryRepository;
        private final IInventoryItemMapper inventoryItemMapper;
        private final MessageUtils messageUtils;
        private final ApiExceptionFactory apiExceptionFactory;

        @Override
        public ApiSuccessDto<PageDto<InventoryItemDto>> getAllInventoryItems(int page, int size, String search,
                        String location, String status, String sortBy, String sortDirection) {

                if (size <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

                if (page <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

                Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
                Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

                Specification<InventoryItemEntity> spec = Specification.where(null);

                if (search != null && !search.isBlank()) {
                        spec = spec.and(InventoryItemSpecifications.nameContains(search));
                }

                if (location != null && !location.isBlank()) {
                        spec = spec.and(InventoryItemSpecifications.locationEquals(location));
                }

                if (status != null && !status.isBlank()) {
                        spec = spec.and(InventoryItemSpecifications.isActiveEquals(status));
                }

                Page<InventoryItemEntity> inventoryItems = inventoryItemRepository.findAll(spec, pageable);

                List<InventoryItemDto> inventoryItemsDto = inventoryItems.getContent().stream()
                                .map(inventoryItemMapper::toDto)
                                .toList();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.inventory.item.get.all.success"),
                                PageDto.fromPage(inventoryItems, inventoryItemsDto));
        }

        @Override
        public ApiSuccessDto<PageDto<InventoryItemDto>> getAllInventoryItemsBySupplier(int supplierId, int page,
                        int size) {
                Page<InventoryItemEntity> inventoryItems = inventoryItemRepository.findAllBySupplierId(supplierId,
                                PageRequest.of(page - 1, size));

                List<InventoryItemDto> inventoryItemsDto = inventoryItems.getContent().stream()
                                .map(inventoryItemMapper::toDto)
                                .toList();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.inventory.item.get.all.success"),
                                PageDto.fromPage(inventoryItems, inventoryItemsDto));
        }

        @Override
        public ApiSuccessDto<InventoryItemDto> getInventoryItemById(int id) {
                InventoryItemEntity inventoryItem = inventoryItemRepository.findById(id)
                                .orElseThrow(() -> apiExceptionFactory
                                                .entityNotFound("operation.inventory.item.get.by.id.not.found"));

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.inventory.item.get.by.id.success"),
                                inventoryItemMapper.toDto(inventoryItem));
        }

        @Override
        public ApiSuccessDto<InventoryItemDto> createInventoryItem(CreateInventoryItemDto createDto) {
                SupplierEntity supplier = supplierRepository.findById(createDto.getSupplierId())
                                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.supplier.not.found"));

                CategoryEntity category = categoryRepository.findById(createDto.getCategoryId())
                                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.category.not.found"));

                InventoryItemEntity inventoryItem = inventoryItemMapper.toEntity(createDto);
                inventoryItem.setSupplier(supplier);
                inventoryItem.setCategory(category);
                inventoryItem.setStockQuantity(BigDecimal.ZERO);

                InventoryItemEntity savedInventoryItem = inventoryItemRepository.save(inventoryItem);

                return ApiSuccessDto.of(HttpStatus.CREATED.value(),
                                messageUtils.getMessage("operation.inventory.item.create.success"),
                                inventoryItemMapper.toDto(savedInventoryItem));
        }

        @Override
        public ApiSuccessDto<InventoryItemDto> updateInventoryItem(int id, UpdateInventoryItemDto updateDto) {
                InventoryItemEntity inventoryItem = inventoryItemRepository.findById(id)
                                .orElseThrow(() -> apiExceptionFactory
                                                .entityNotFound("operation.inventory.item.update.not.found"));

                inventoryItemMapper.updateEntityFromDto(updateDto, inventoryItem);

                if (updateDto.getSupplierId() != inventoryItem.getSupplier().getId()) {
                        SupplierEntity supplier = supplierRepository.findById(updateDto.getSupplierId())
                                        .orElseThrow(() -> apiExceptionFactory
                                                        .entityNotFound("operation.supplier.not.found"));
                        inventoryItem.setSupplier(supplier);
                }

                if (updateDto.getCategoryId() != inventoryItem.getCategory().getId()) {
                        CategoryEntity category = categoryRepository.findById(updateDto.getCategoryId())
                                        .orElseThrow(() -> apiExceptionFactory
                                                        .entityNotFound("operation.category.not.found"));
                        inventoryItem.setCategory(category);
                }

                InventoryItemEntity savedInventoryItem = inventoryItemRepository.save(inventoryItem);

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.inventory.item.update.success"),
                                inventoryItemMapper.toDto(savedInventoryItem));
        }

        @Override
        public ApiSuccessDto<Void> deleteInventoryItem(int id) {
                InventoryItemEntity inventoryItem = inventoryItemRepository.findById(id)
                                .orElseThrow(() -> apiExceptionFactory
                                                .entityNotFound("operation.inventory.item.delete.not.found"));

                inventoryItemRepository.delete(inventoryItem);

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtils.getMessage("operation.inventory.item.delete.success"), null);
        }
}