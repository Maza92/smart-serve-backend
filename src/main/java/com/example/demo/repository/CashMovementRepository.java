package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CashMovementEntity;

@Repository
public interface CashMovementRepository extends JpaRepository<CashMovementEntity, Integer>, JpaSpecificationExecutor<CashMovementEntity> {
    
    @Query("SELECT cm FROM CashMovementEntity cm WHERE cm.active = true")
    List<CashMovementEntity> findAllActive();
    
    @Query("SELECT cm FROM CashMovementEntity cm WHERE cm.cashRegister.id = :cashRegisterId AND cm.active = true")
    List<CashMovementEntity> findByCashRegisterId(@Param("cashRegisterId") Integer cashRegisterId);
    
    @Query("SELECT cm FROM CashMovementEntity cm WHERE cm.user.id = :userId AND cm.active = true")
    List<CashMovementEntity> findByUserId(@Param("userId") Integer userId);
}