package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.report.dashboard.PaymentMethodDataDto;
import com.example.demo.entity.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer>, JpaSpecificationExecutor<TransactionEntity> {

    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.order o
            WHERE t.status = 'COMPLETED'
            ORDER BY t.transactionDate DESC
            """)
    List<TransactionEntity> findRecentTransactions(Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM TransactionEntity t
            WHERE t.transactionDate BETWEEN :startDate AND :endDate
            AND t.status = 'COMPLETED'
            AND t.transactionType = 'SALE'
            """)
    BigDecimal getSalesBetweenDates(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query("""
                SELECT COUNT(t.id)
                FROM TransactionEntity t
                WHERE t.transactionDate BETWEEN :startDate AND :endDate
            """)
    Long countTransactionsByDateRange(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query("""
                SELECT new com.example.demo.dto.report.dashboard.PaymentMethodDataDto(
                    t.paymentMethod,
                    SUM(t.amount)
                )
                FROM TransactionEntity t
                WHERE t.transactionDate BETWEEN :startDate AND :endDate
                GROUP BY t.paymentMethod
                ORDER BY SUM(t.amount) DESC
            """)
    List<PaymentMethodDataDto> findPaymentMethodDistribution(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);
    
    @Override
    Page<TransactionEntity> findAll(Specification<TransactionEntity> spec, Pageable pageable);
}