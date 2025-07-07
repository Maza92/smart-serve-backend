package com.example.demo.dto.transaction;

import java.math.BigDecimal;
import java.util.Map;

import com.example.demo.enums.PaymentMethodEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderTransactionDto {
    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethodEnum paymentMethod;

    private String referenceNumber;

    private Map<String, Object> paymentDetails;
}