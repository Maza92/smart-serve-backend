package com.example.demo.service.transaction.impl;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.transaction.CreateOrderTransactionDto;
import com.example.demo.dto.transaction.CreateTransactionDto;
import com.example.demo.dto.transaction.TransactionDto;
import com.example.demo.entity.CashRegisterEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.TransactionEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.CashRegisterEnum;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.enums.RestaurantTableEnum;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.ITransactionMapper;
import com.example.demo.repository.CashRegisterRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.order.IOrderService;
import com.example.demo.service.restaurantTable.IRestaurantTableService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.service.transaction.ITransactionService;
import com.example.demo.specifications.TransactionSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final CashRegisterRepository cashRegisterRepository;
    private final OrderRepository orderRepository;
    private final ITransactionMapper transactionMapper;
    private final IRestaurantTableService restaurantTableService;
    private final ApiExceptionFactory apiExceptionFactory;
    private final ISecurityContextService securityContextService;
    private final IOrderService orderService;
    private final MessageUtils messageUtils;

    @Override
    @Transactional
    public ApiSuccessDto<TransactionDto> createTransaction(CreateTransactionDto createTransactionDto) {
        if (!cashRegisterRepository.existsOpenedCashRegister()) {
            throw apiExceptionFactory.badRequestException("operation.transaction.no.open.register");
        }

        CashRegisterEntity cashRegister = cashRegisterRepository.findById(createTransactionDto.getCashRegisterId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.register.not.found"));

        if (cashRegister.getStatus() != CashRegisterEnum.OPENED) {
            throw apiExceptionFactory.badRequestException("operation.transaction.register.closed");
        }

        UserEntity user = securityContextService.getUser();

        TransactionEntity transaction = transactionMapper.toEntity(createTransactionDto);

        if (createTransactionDto.getOrderId() != null) {
            OrderEntity order = orderRepository.findById(createTransactionDto.getOrderId())
                    .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.order.not.found"));
            transaction.setOrder(order);
        }

        transaction.setCashRegister(cashRegister);
        transaction.setUser(user);
        transaction.setTransactionDate(Instant.now());
        transaction.setActive(true);

        if (transaction.getStatus() == TransactionStatusEnum.COMPLETED) {
            updateCashRegisterBalance(cashRegister, transaction);
            cashRegisterRepository.save(cashRegister);
        }

        TransactionEntity savedTransaction = transactionRepository.save(transaction);

        return ApiSuccessDto.of(HttpStatus.CREATED.value(),
                messageUtils.getMessage("operation.transaction.created"),
                transactionMapper.toDto(savedTransaction));
    }

    @Override
    @Transactional
    public ApiSuccessDto<TransactionDto> createOrderTransaction(CreateOrderTransactionDto createOrderTransactionDto) {
        if (!cashRegisterRepository.existsOpenedCashRegister()) {
            throw apiExceptionFactory.badRequestException("operation.transaction.no.open.register");
        }

        CashRegisterEntity cashRegister = cashRegisterRepository.findCurrentOpenedCashRegister()
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.register.not.found"));

        if (cashRegister.getStatus() != CashRegisterEnum.OPENED) {
            throw apiExceptionFactory.badRequestException("operation.transaction.register.closed");
        }

        OrderEntity order = orderRepository
                .findByStatusAndId(OrderStatusEnum.PAYMENT_PENDING, createOrderTransactionDto.getOrderId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.order.not.found"));

        UserEntity user = securityContextService.getUser();

        TransactionEntity transaction = transactionMapper.toEntityFromOrderTransaction(createOrderTransactionDto);
        transaction.setCashRegister(cashRegister);
        transaction.setTransactionType(TransactionTypeEnum.SALE);
        transaction.setOrder(order);
        transaction.setUser(user);
        transaction.setTransactionDate(Instant.now());
        transaction.setActive(true);

        // Simulate payment processing

        switch (transaction.getPaymentMethod()) {
            case CREDIT_CARD, DEBIT_CARD -> {
                log.info("Processing card payment for order: {}", order.getId());
            }
            case BANK_TRANSFER -> {
                log.info("Processing bank transfer for order: {}", order.getId());
            }
            case YAPE, PLIN -> {
                log.info("Processing mobile payment for order: {}", order.getId());
            }
            default -> {
                log.info("Processing {} payment for order: {}", transaction.getPaymentMethod(), order.getId());
            }
        }

        updateCashRegisterBalance(cashRegister, transaction);
        cashRegisterRepository.save(cashRegister);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);

        order.setStatus(OrderStatusEnum.PAID);
        orderRepository.save(order);
        orderService.sendUpdateMessageToKitchen(order);
        restaurantTableService.setStatusAndSendMessage(order.getTable(), RestaurantTableEnum.AVAILABLE);

        return ApiSuccessDto.of(HttpStatus.CREATED.value(),
                messageUtils.getMessage("operation.transaction.order.created"),
                transactionMapper.toDto(savedTransaction));
    }

    @Override
    public ApiSuccessDto<PageDto<TransactionDto>> getTransactions(int page, int size, Integer cashRegisterId,
            Integer orderId, Integer userId, PaymentMethodEnum paymentMethod, TransactionTypeEnum transactionType,
            TransactionStatusEnum status, String startDate, String endDate, String sortBy, String sortDirection) {

        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

        Specification<TransactionEntity> spec = Specification.where(TransactionSpecifications.activeEquals(true));

        if (cashRegisterId != null) {
            spec = spec.and(TransactionSpecifications.cashRegisterIdEquals(cashRegisterId));
        }

        if (orderId != null) {
            spec = spec.and(TransactionSpecifications.orderIdEquals(orderId));
        }

        if (userId != null) {
            spec = spec.and(TransactionSpecifications.userIdEquals(userId));
        }

        if (paymentMethod != null) {
            spec = spec.and(TransactionSpecifications.paymentMethodEquals(paymentMethod));
        }

        if (transactionType != null) {
            spec = spec.and(TransactionSpecifications.transactionTypeEquals(transactionType));
        }

        if (status != null) {
            spec = spec.and(TransactionSpecifications.statusEquals(status));
        }

        Instant startInstant = null;
        Instant endInstant = null;

        if (startDate != null && !startDate.isBlank()) {
            startInstant = Instant.parse(startDate);
        }

        if (endDate != null && !endDate.isBlank()) {
            endInstant = Instant.parse(endDate);
        }

        if (startInstant != null || endInstant != null) {
            spec = spec.and(TransactionSpecifications.dateRangeBetween(startInstant, endInstant));
        }

        Page<TransactionEntity> transactions = transactionRepository.findAll(spec, pageable);

        var transactionDtos = transactions.getContent().stream()
                .map(transactionMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.transaction.get.all.success"),
                PageDto.fromPage(transactions, transactionDtos));
    }

    @Override
    public ApiSuccessDto<TransactionDto> getTransactionById(Integer id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.transaction.not.found"));

        if (!transaction.isActive()) {
            throw apiExceptionFactory.entityNotFound("operation.transaction.not.found");
        }

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.transaction.get.by.id.success"),
                transactionMapper.toDto(transaction));
    }

    @Override
    @Transactional
    public ApiSuccessDto<Void> deleteTransaction(Integer id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.transaction.not.found"));

        if (!transaction.isActive()) {
            throw apiExceptionFactory.entityNotFound("operation.transaction.not.found");
        }

        CashRegisterEntity cashRegister = transaction.getCashRegister();
        if (cashRegister.getStatus() != CashRegisterEnum.OPENED) {
            throw apiExceptionFactory.badRequestException("operation.transaction.register.closed");
        }

        if (transaction.getStatus() == TransactionStatusEnum.COMPLETED) {
            reverseCashRegisterBalance(cashRegister, transaction);
            cashRegisterRepository.save(cashRegister);
        }

        transaction.setActive(false);
        transactionRepository.save(transaction);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.transaction.deleted"),
                null);
    }

    private void updateCashRegisterBalance(CashRegisterEntity cashRegister, TransactionEntity transaction) {
        switch (transaction.getTransactionType()) {
            case SALE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().add(transaction.getAmount()));
            }
            case REFUND -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().subtract(transaction.getAmount()));
            }
            case MANUAL_ADJUSTMENT -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().add(transaction.getAmount()));
            }
            case EXPENSE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().subtract(transaction.getAmount()));
            }
            default -> {
                log.warn("Transaction type not handled: {}", transaction.getTransactionType());
            }
        }

        cashRegister.setDifference(cashRegister.getExpectedAmount().subtract(cashRegister.getFinalAmount()));
    }

    private void reverseCashRegisterBalance(CashRegisterEntity cashRegister, TransactionEntity transaction) {
        switch (transaction.getTransactionType()) {
            case SALE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().subtract(transaction.getAmount()));
            }
            case REFUND -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().add(transaction.getAmount()));
            }
            case MANUAL_ADJUSTMENT -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().subtract(transaction.getAmount()));
            }
            case EXPENSE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().add(transaction.getAmount()));
            }
            default -> {
                log.warn("Transaction type not handled for reversal: {}", transaction.getTransactionType());
            }
        }

        cashRegister.setDifference(cashRegister.getExpectedAmount().subtract(cashRegister.getFinalAmount()));
    }
}