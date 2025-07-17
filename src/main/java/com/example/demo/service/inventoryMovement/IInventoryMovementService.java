package com.example.demo.service.inventoryMovement;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.inventoryMovement.InventoryMovementDto;

public interface IInventoryMovementService {
    ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsByItem(Integer id, Integer page, Integer size);

    ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsByOrder(Integer id, Integer page, Integer size);

    ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsByUser(Integer id, Integer page, Integer size);

    ApiSuccessDto<PageDto<InventoryMovementDto>> getMovementsBySupplier(Integer id, Integer page, Integer size);

    ApiSuccessDto<PageDto<InventoryMovementDto>> getLastMovements(Integer page, Integer size);
}
