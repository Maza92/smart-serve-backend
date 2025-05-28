package com.example.demo.service.inventory.impl;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;
import com.example.demo.entity.InventoryItemEntity;
import com.example.demo.entity.InventoryMovementEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.repository.InventoryItemRepository;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.service.inventory.IInventoryService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.utils.MessageUtils;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryMovementRepository movementRepository;
    private final ApiExceptionFactory apiExceptionFactory;
    private final ISecurityContextService securityContextService;
    private final MessageUtils messageUtils;

    @Override
    @Transactional
    public ApiSuccessDto<Void> updateStock(UpdateInventoryItemStockDto request) {
        InventoryItemEntity item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound(""));

        BigDecimal quantityBefore = item.getStockQuantity();
        BigDecimal quantityAfter = quantityBefore.add(request.getQuantityChanged());

        InventoryMovementEntity movement = InventoryMovementEntity.builder()
                .inventoryItem(item)
                .movementType(request.getMovementType())
                .quantityBefore(quantityBefore)
                .quantityAfter(quantityAfter)
                .quantityChanged(request.getQuantityChanged())
                .reason(request.getReason())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .user(securityContextService.getUser())
                .build();

        movementRepository.save(movement);

        item.setStockQuantity(quantityAfter);
        itemRepository.save(item);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("inventory.update.success"));
    }
}
