package com.example.demo.service.cashRegister;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;
import com.example.demo.enums.CashRegisterEnum;
import com.example.demo.dto.cashRegister.CashRegisterDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;

public interface ICashRegisterService {
    ApiSuccessDto<Void> createPartialCashRegister(PartialCreateCashRegisterDto cashRegister);

    ApiSuccessDto<Void> OpenCashRegister(OpenCashRegisterDto cashRegister, Integer id);

    ApiSuccessDto<Void> CloseCashRegister(ClosedCashRegisterDto cashRegister, Integer id);

    ApiSuccessDto<PageDto<CashRegisterDto>> getAllCashRegisters(int page, int size);

    ApiSuccessDto<CashRegisterEnum> getCashRegisterStatus();

    ApiSuccessDto<CashRegisterDto> getCurrentOpenedCashRegister();

    ApiSuccessDto<List<CashRegisterDto>> getAvailableCashRegistersToOpen();
}