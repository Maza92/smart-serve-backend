package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CashRegisterEntity;
import com.example.demo.enums.CashRegisterEnum;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegisterEntity, Integer> {
    CashRegisterEntity findByStatus(CashRegisterEnum status);

    @Query("SELECT c FROM CashRegisterEntity c WHERE FUNCTION('DATE', c.openDate) = CURRENT_DATE ORDER BY c.id DESC")
    Optional<CashRegisterEntity> findTodayCashRegister();

    @Query("SELECT c FROM CashRegisterEntity c WHERE c.status = 'CREATED' ORDER BY c.id DESC")
    Optional<CashRegisterEntity> findLastCreatedRegister();

    @Query("""
                SELECT c FROM CashRegisterEntity c
                WHERE (
                    (c.status = 'CREATED' AND FUNCTION('DATE', c.createdAt) = CURRENT_DATE) OR
                    (c.status = 'OPENED' AND FUNCTION('DATE', c.openDate) = CURRENT_DATE) OR
                    (c.status = 'CLOSED' AND FUNCTION('DATE', c.closeDate) = CURRENT_DATE)
                )
                ORDER BY c.id DESC
            """)
    List<CashRegisterEntity> findTodaysCashRegisters();

    @Query("""
                SELECT c FROM CashRegisterEntity c
                WHERE c.status = 'CREATED'
                AND FUNCTION('DATE', c.createdAt) = CURRENT_DATE
                ORDER BY c.id DESC
            """)
    List<CashRegisterEntity> findCreatedToday();

    @Query("""
                SELECT c FROM CashRegisterEntity c
                WHERE c.status = 'OPENED'
                AND FUNCTION('DATE', c.openDate) = CURRENT_DATE
                ORDER BY c.id DESC
            """)
    List<CashRegisterEntity> findOpenedToday();

    @Query("""
                SELECT c FROM CashRegisterEntity c
                WHERE c.status = 'CLOSED'
                AND FUNCTION('DATE', c.closeDate) = CURRENT_DATE
                ORDER BY c.id DESC
            """)
    List<CashRegisterEntity> findClosedToday();

    @Query("SELECT COUNT(c) > 0 FROM CashRegisterEntity c WHERE c.status = 'OPENED'")
    boolean existsOpenedCashRegister();

    @Query("SELECT c FROM CashRegisterEntity c WHERE c.status = 'OPENED' ORDER BY c.id DESC")
    Optional<CashRegisterEntity> findCurrentOpenedCashRegister();

    @Query("""
                SELECT COUNT(c) FROM CashRegisterEntity c
                WHERE c.status = 'CREATED'
                AND FUNCTION('DATE', c.createdAt) = CURRENT_DATE
            """)
    long countCreatedToday();

    @Query("""
                SELECT c FROM CashRegisterEntity c
                WHERE c.status = 'CREATED'
                ORDER BY c.id ASC
            """)
    List<CashRegisterEntity> findAvailableCashRegistersToOpen();
}