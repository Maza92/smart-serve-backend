package com.example.demo.service.cashRegister.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.cashRegister.CashRegisterCreateDto;
import com.example.demo.entity.CashRegisterEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.repository.CashRegisterRepository;
import com.example.demo.service.cashRegister.ICashRegisterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CashRegisterServiceImpl implements ICashRegisterService {

    private final CashRegisterRepository cashRegisterRepository;
    private final ApiExceptionFactory apiExceptionFactory;

    @Override
    public List<CashRegisterEntity> getAllCashRegisters() {
        return cashRegisterRepository.findAll();
    }

    @Override
    public void createCashRegister(CashRegisterCreateDto cashRegister) {
        try {
            CashRegisterEntity newCashRegister = new CashRegisterEntity();

        } catch (DataIntegrityViolationException e) {
            throw apiExceptionFactory.conflictException("exception.cashRegister.alreadyExists");
        } catch (Exception e) {
            throw apiExceptionFactory.businessException("exception.unexpected", e.getMessage());
        }
    }
}