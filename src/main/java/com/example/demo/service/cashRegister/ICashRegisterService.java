package com.example.demo.service.cashRegister;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;
import com.example.demo.entity.CashRegisterEntity;

public interface ICashRegisterService {
    List<CashRegisterEntity> getAllCashRegisters();

    ApiSuccessDto<Void> createPartialCashRegister(PartialCreateCashRegisterDto cashRegister);

    ApiSuccessDto<Void> OpenCashRegister(OpenCashRegisterDto cashRegister, Integer id);

    ApiSuccessDto<Void> CloseCashRegister(ClosedCashRegisterDto cashRegister, Integer id);
}