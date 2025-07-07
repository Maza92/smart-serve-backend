package com.example.demo.dto.transaction;

import java.math.BigDecimal;
import java.util.Map;

import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;

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
public class CreateTransactionDto {
    @NotNull(message = "Cash register ID is required")
    private Integer cashRegisterId;
    
    private Integer orderId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethodEnum paymentMethod;
    
    @NotNull(message = "Transaction type is required")
    private TransactionTypeEnum transactionType;
    
    private String referenceNumber;
    
    @NotNull(message = "Status is required")
    private TransactionStatusEnum status;
    
    private Map<String, Object> paymentDetails;
}