package com.example.demo.dto.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Integer id;
    private Integer cashRegisterId;
    private String cashRegisterStatus;
    private Integer orderId;
    private String orderNumber;
    private BigDecimal amount;
    private PaymentMethodEnum paymentMethod;
    private TransactionTypeEnum transactionType;
    private String referenceNumber;
    private TransactionStatusEnum status;
    private Map<String, Object> paymentDetails;
    private Instant transactionDate;
    private String username;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean active;
}