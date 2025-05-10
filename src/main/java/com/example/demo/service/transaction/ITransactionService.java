package com.example.demo.service.transaction;

import java.util.List;

import com.example.demo.entity.TransactionEntity;

public interface ITransactionService {
    List<TransactionEntity> getAllTransactions();
}