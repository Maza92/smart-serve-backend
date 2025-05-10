package com.example.demo.service.cashMovement;

import java.util.List;

import com.example.demo.entity.CashMovementEntity;

public interface ICashMovementService {
    List<CashMovementEntity> getAllCashMovements();
}