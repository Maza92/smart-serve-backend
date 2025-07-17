package com.example.demo.service.inventoryMovement.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.inventoryMovement.InventoryMovementDto;
import com.example.demo.entity.InventoryMovementEntity;
import com.example.demo.enums.ReferenceTypeEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IInventoryMovementMapper;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.service.inventoryMovement.IInventoryMovementService;
import com.example.demo.utils.MessageUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryMovementServiceImpl implements IInventoryMovementService {

    private final InventoryMovementRepository inventoryMovementRepository;
    private final IInventoryMovementMapper inventoryMovementMapper;
    private final MessageUtils messageUtils;
    private final ApiExceptionFactory apiExceptionFactory;

    @Override
    public ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsByItem(Integer id, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<InventoryMovementEntity> inventoryMovements = inventoryMovementRepository.findAllByItemId(id, pageable);
        return processMovementsForPagedDtos(inventoryMovements, "operation.inventory.item.get.all.success");
    }

    @Override
    public ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsByOrder(Integer id, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<InventoryMovementEntity> inventoryMovements = inventoryMovementRepository.findAllByOrderId(id,
                ReferenceTypeEnum.ORDER, pageable);
        return processMovementsForPagedDtos(inventoryMovements, "operation.inventory.order.get.all.success");
    }

    public ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsByUser(Integer id, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<InventoryMovementEntity> inventoryMovements = inventoryMovementRepository.findAllByUserId(id, pageable);
        return processMovementsForPagedDtos(inventoryMovements, "operation.inventory.user.get.all.success");
    }

    @Override
    public ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsBySupplier(Integer id, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<InventoryMovementEntity> inventoryMovements = inventoryMovementRepository.findAllBySupplierId(id,
                pageable);
        return processMovementsForPagedDtos(inventoryMovements, "operation.inventory.supplier.get.all.success");
    }

    public ApiSuccessDto<PageDto<InventoryMovementDto>> getLastMovements(Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<InventoryMovementEntity> inventoryMovements = inventoryMovementRepository.findAllLastMovements(pageable);
        return processMovementsForPagedDtos(inventoryMovements, "operation.inventory.last.get.all.success");
    }

    private ApiSuccessDto<PageDto<InventoryMovementDto>> processMovementsForPagedDtos(
            Page<InventoryMovementEntity> inventoryMovements, String message) {
        List<InventoryMovementDto> inventoryMovementsDto = inventoryMovements.getContent().stream()
                .map(movement -> {
                    InventoryMovementDto dto = inventoryMovementMapper.toDto(movement);
                    dto.setUserName(movement.getUser().getFirstName() + " " + movement.getUser().getLastName());
                    dto.setMovementValue(movement.getQuantityChanged().multiply(movement.getUnitCostAtTime()));

                    return dto;
                })
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage(message),
                PageDto.fromPage(inventoryMovements, inventoryMovementsDto));
    }

    private Pageable getPageable(Integer page, Integer size) {
        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        return Pageable.ofSize(size).withPage(Math.max(page - 1, 0));
    }

}
