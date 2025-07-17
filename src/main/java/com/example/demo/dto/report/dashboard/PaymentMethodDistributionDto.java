package com.example.demo.dto.report.dashboard;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.enums.PaymentMethodEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDistributionDto {
    private List<PaymentMethodEnum> paymentMethods;
    private List<BigDecimal> percentages;
    private BigDecimal totalAmount;
}
