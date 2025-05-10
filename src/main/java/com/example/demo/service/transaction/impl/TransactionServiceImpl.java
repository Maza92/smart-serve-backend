package com.example.demo.service.transaction.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.TransactionEntity;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.transaction.ITransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    
    @Override
    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }
}