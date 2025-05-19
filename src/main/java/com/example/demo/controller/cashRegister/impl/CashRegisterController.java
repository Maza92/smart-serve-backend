package com.example.demo.controller.cashRegister.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.cashRegister.ICashRegisterController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashRegister.CashRegisterDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;
import com.example.demo.service.cashRegister.ICashRegisterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cash")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CashRegisterController implements ICashRegisterController {

    private final ICashRegisterService cashRegisterService;

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> createCashRegister(PartialCreateCashRegisterDto cashRegisterCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cashRegisterService.createPartialCashRegister(cashRegisterCreateDto));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> openCashRegister(OpenCashRegisterDto cashRegisterCreateDto,
            Integer cashRegisterId) {
        return ResponseEntity.ok(cashRegisterService.OpenCashRegister(cashRegisterCreateDto, cashRegisterId));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> closeCashRegister(ClosedCashRegisterDto cashRegisterCreateDto,
            Integer cashRegisterId) {
        return ResponseEntity.ok(cashRegisterService.CloseCashRegister(cashRegisterCreateDto, cashRegisterId));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<CashRegisterDto>>> getAllCashRegisters(int page, int size) {
        return ResponseEntity.ok(cashRegisterService.getAllCashRegisters(page, size));
    }
}
