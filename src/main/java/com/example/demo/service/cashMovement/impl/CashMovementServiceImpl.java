package com.example.demo.service.cashMovement.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CashMovementEntity;
import com.example.demo.repository.CashMovementRepository;
import com.example.demo.service.cashMovement.ICashMovementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CashMovementServiceImpl implements ICashMovementService {

    private final CashMovementRepository cashMovementRepository;
    
    @Override
    public List<CashMovementEntity> getAllCashMovements() {
        return cashMovementRepository.findAll();
    }
}