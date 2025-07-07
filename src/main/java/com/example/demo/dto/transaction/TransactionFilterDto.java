package com.example.demo.dto.transaction;

import java.time.Instant;

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
public class TransactionFilterDto {
    private Integer cashRegisterId;
    private Integer orderId;
    private Integer userId;
    private PaymentMethodEnum paymentMethod;
    private TransactionTypeEnum transactionType;
    private TransactionStatusEnum status;
    private Instant startDate;
    private Instant endDate;
    private Boolean active;
}