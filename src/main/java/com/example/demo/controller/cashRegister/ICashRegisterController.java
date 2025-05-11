package com.example.demo.controller.cashRegister;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cash")
public interface ICashRegisterController {
    
    
    void createCashRegister();
}
