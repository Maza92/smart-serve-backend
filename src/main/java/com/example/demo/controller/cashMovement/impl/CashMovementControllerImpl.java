package com.example.demo.controller.cashMovement.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.cashMovement.ICashMovementController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashMovement.CashMovementDto;
import com.example.demo.dto.cashMovement.CreateCashMovementDto;
import com.example.demo.enums.CashMovementTypeEnum;
import com.example.demo.service.cashMovement.ICashMovementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cash-movements")
@RequiredArgsConstructor
public class CashMovementControllerImpl implements ICashMovementController {

    private final ICashMovementService cashMovementService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<CashMovementDto>> createCashMovement(
            CreateCashMovementDto createCashMovementDto) {
        return ResponseEntity.ok(cashMovementService.createCashMovement(createCashMovementDto));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<PageDto<CashMovementDto>>> getCashMovements(int page, int size,
            Integer cashRegisterId, Integer userId, CashMovementTypeEnum movementType, String startDate, String endDate,
            String sortBy, String sortDirection) {
        return ResponseEntity.ok(cashMovementService.getCashMovements(
                page, size, cashRegisterId, userId, movementType, startDate, endDate, sortBy, sortDirection));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<CashMovementDto>> getCashMovementById(Integer id) {
        return ResponseEntity.ok(cashMovementService.getCashMovementById(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<Void>> deleteCashMovement(Integer id) {
        return ResponseEntity.ok(cashMovementService.deleteCashMovement(id));
    }
}