package com.example.demo.dto.report.dashboard;

import java.math.BigDecimal;

import com.example.demo.enums.PaymentMethodEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDataDto {
    private PaymentMethodEnum paymentMethod;
    private BigDecimal totalAmount;
}
