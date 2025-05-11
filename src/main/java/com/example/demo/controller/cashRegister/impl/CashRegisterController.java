package com.example.demo.controller.cashRegister.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.cashRegister.ICashRegisterController;
import com.example.demo.dto.cashRegister.CashRegisterCreateDto;
import com.example.demo.service.cashRegister.ICashRegisterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cash")
@RequiredArgsConstructor
public class CashRegisterController implements ICashRegisterController {

    private final ICashRegisterService cashRegisterService;

    @Override
    @PreAuthorize("hasRole('CASHIER')")
    public String createCashRegister(CashRegisterCreateDto cashRegisterCreateDto) {
        return "Cash register created successfully";
    }
}
