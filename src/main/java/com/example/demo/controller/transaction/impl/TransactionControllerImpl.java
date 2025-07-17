package com.example.demo.controller.transaction.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.transaction.ITransactionController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.transaction.CreateOrderTransactionDto;
import com.example.demo.dto.transaction.CreateTransactionDto;
import com.example.demo.dto.transaction.TransactionDto;
import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;
import com.example.demo.service.transaction.ITransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TransactionControllerImpl implements ITransactionController {

    private final ITransactionService transactionService;

    @Override
    public ResponseEntity<ApiSuccessDto<TransactionDto>> createTransaction(CreateTransactionDto createTransactionDto) {
        ApiSuccessDto<TransactionDto> response = transactionService.createTransaction(createTransactionDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiSuccessDto<TransactionDto>> createOrderTransaction(
            CreateOrderTransactionDto createOrderTransactionDto) {
        ApiSuccessDto<TransactionDto> response = transactionService.createOrderTransaction(createOrderTransactionDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<TransactionDto>>> getTransactions(int page, int size,
            Integer cashRegisterId, Integer orderId, Integer userId, PaymentMethodEnum paymentMethod,
            TransactionTypeEnum transactionType, TransactionStatusEnum status, String startDate, String endDate,
            String sortBy, String sortDirection) {
        ApiSuccessDto<PageDto<TransactionDto>> response = transactionService.getTransactions(page, size, cashRegisterId,
                orderId, userId, paymentMethod, transactionType, status, startDate, endDate, sortBy, sortDirection);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiSuccessDto<TransactionDto>> getTransactionById(Integer id) {
        ApiSuccessDto<TransactionDto> response = transactionService.getTransactionById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiSuccessDto<Void>> deleteTransaction(Integer id) {
        ApiSuccessDto<Void> response = transactionService.deleteTransaction(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
