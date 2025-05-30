package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.InventoryMovementEntity;
import com.example.demo.enums.ReferenceTypeEnum;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovementEntity, Integer> {

    @Query("SELECT i FROM InventoryMovementEntity i WHERE i.inventoryItem.id = :id")
    Page<InventoryMovementEntity> findAllByItemId(@Param("id") Integer id, Pageable pageable);

    @Query("SELECT i FROM InventoryMovementEntity i WHERE i.referenceId = :id AND i.referenceType = :type")
    Page<InventoryMovementEntity> findAllByOrderId(@Param("id") Integer id, @Param("type") ReferenceTypeEnum orden,
            Pageable pageable);

}
