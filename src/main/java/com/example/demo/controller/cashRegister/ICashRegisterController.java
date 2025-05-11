package com.example.demo.controller.cashRegister;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.controller.docBase.AcceptLanguageHeader;
import com.example.demo.dto.cashRegister.CashRegisterCreateDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cash", description = "Cash Operations API")
public interface ICashRegisterController {

    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    @PostMapping("/create")
    String createCashRegister(@RequestBody CashRegisterCreateDto cashRegisterCreateDto);
}
