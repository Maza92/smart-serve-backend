package com.example.demo.service.cashRegister;

import java.util.List;

import com.example.demo.entity.CashRegisterEntity;

public interface ICashRegisterService {
    List<CashRegisterEntity> getAllCashRegisters();
}