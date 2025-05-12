package com.example.demo.controller.cashRegister;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.controller.docBase.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cash", description = "Cash Operations API")
public interface ICashRegisterController {

    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    @PostMapping("/create")
    ResponseEntity<ApiSuccessDto<Void>> createCashRegister(
            @RequestBody PartialCreateCashRegisterDto cashRegisterCreateDto);

    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    @PutMapping("/open/{cashRegisterId}")
    ResponseEntity<ApiSuccessDto<Void>> openCashRegister(
            @RequestBody OpenCashRegisterDto cashRegisterCreateDto, Integer cashRegisterId);

    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    @PutMapping("/close/{cashRegisterId}")
    ResponseEntity<ApiSuccessDto<Void>> closeCashRegister(
            @RequestBody ClosedCashRegisterDto cashRegisterCreateDto, Integer cashRegisterId);
}
