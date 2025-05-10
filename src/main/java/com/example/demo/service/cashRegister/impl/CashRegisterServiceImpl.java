package com.example.demo.service.cashRegister.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CashRegisterEntity;
import com.example.demo.repository.CashRegisterRepository;
import com.example.demo.service.cashRegister.ICashRegisterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CashRegisterServiceImpl implements ICashRegisterService {

    private final CashRegisterRepository cashRegisterRepository;
    
    @Override
    public List<CashRegisterEntity> getAllCashRegisters() {
        return cashRegisterRepository.findAll();
    }
}