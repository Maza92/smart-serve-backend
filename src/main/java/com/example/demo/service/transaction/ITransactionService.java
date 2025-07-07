package com.example.demo.service.transaction;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.transaction.CreateOrderTransactionDto;
import com.example.demo.dto.transaction.CreateTransactionDto;
import com.example.demo.dto.transaction.TransactionDto;
import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;

public interface ITransactionService {
    /**
     * Creates a new transaction
     * 
     * @param createTransactionDto The DTO containing the transaction data
     * @return ApiSuccessDto with the operation result
     */
    ApiSuccessDto<TransactionDto> createTransaction(CreateTransactionDto createTransactionDto);
    
    /**
     * Creates a new transaction for an order (payment)
     * 
     * @param createOrderTransactionDto The DTO containing the order transaction data
     * @return ApiSuccessDto with the operation result
     */
    ApiSuccessDto<TransactionDto> createOrderTransaction(CreateOrderTransactionDto createOrderTransactionDto);

    /**
     * Gets a paginated list of transactions with optional filters
     * 
     * @param page             The page number (1-based)
     * @param size             The page size
     * @param cashRegisterId   Optional filter by cash register ID
     * @param orderId          Optional filter by order ID
     * @param userId           Optional filter by user ID
     * @param paymentMethod    Optional filter by payment method
     * @param transactionType  Optional filter by transaction type
     * @param status           Optional filter by status
     * @param startDate        Optional filter by start date
     * @param endDate          Optional filter by end date
     * @param sortBy           Field to sort by
     * @param sortDirection    Sort direction (ASC or DESC)
     * @return ApiSuccessDto with the paginated list of transactions
     */
    ApiSuccessDto<PageDto<TransactionDto>> getTransactions(
            int page,
            int size,
            Integer cashRegisterId,
            Integer orderId,
            Integer userId,
            PaymentMethodEnum paymentMethod,
            TransactionTypeEnum transactionType,
            TransactionStatusEnum status,
            String startDate,
            String endDate,
            String sortBy,
            String sortDirection);

    /**
     * Gets a transaction by ID
     * 
     * @param id The transaction ID
     * @return ApiSuccessDto with the transaction
     */
    ApiSuccessDto<TransactionDto> getTransactionById(Integer id);

    /**
     * Soft deletes a transaction by setting active to false
     * 
     * @param id The transaction ID
     * @return ApiSuccessDto with the operation result
     */
    ApiSuccessDto<Void> deleteTransaction(Integer id);
}