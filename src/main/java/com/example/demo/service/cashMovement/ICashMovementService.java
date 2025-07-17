package com.example.demo.service.cashMovement;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashMovement.CashMovementDto;
import com.example.demo.dto.cashMovement.CreateCashMovementDto;
import com.example.demo.enums.CashMovementTypeEnum;

public interface ICashMovementService {
    /**
     * Creates a new cash movement
     * 
     * @param createCashMovementDto The DTO containing the cash movement data
     * @return ApiSuccessDto with the operation result
     */
    ApiSuccessDto<CashMovementDto> createCashMovement(CreateCashMovementDto createCashMovementDto);

    /**
     * Gets a paginated list of cash movements with optional filters
     * 
     * @param page           The page number (1-based)
     * @param size           The page size
     * @param cashRegisterId Optional filter by cash register ID
     * @param userId         Optional filter by user ID
     * @param movementType   Optional filter by movement type
     * @param startDate      Optional filter by start date
     * @param endDate        Optional filter by end date
     * @param sortBy         Field to sort by
     * @param sortDirection  Sort direction (ASC or DESC)
     * @return ApiSuccessDto with the paginated list of cash movements
     */
    ApiSuccessDto<PageDto<CashMovementDto>> getCashMovements(
            int page,
            int size,
            Integer cashRegisterId,
            Integer userId,
            CashMovementTypeEnum movementType,
            String startDate,
            String endDate,
            String sortBy,
            String sortDirection);

    /**
     * Gets a cash movement by ID
     * 
     * @param id The cash movement ID
     * @return ApiSuccessDto with the cash movement
     */
    ApiSuccessDto<CashMovementDto> getCashMovementById(Integer id);

    /**
     * Soft deletes a cash movement by setting active to false
     * 
     * @param id The cash movement ID
     * @return ApiSuccessDto with the operation result
     */
    ApiSuccessDto<Void> deleteCashMovement(Integer id);
}