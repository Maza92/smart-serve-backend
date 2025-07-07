package com.example.demo.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.inventory.dashboard.MovementStatsDto;
import com.example.demo.entity.InventoryMovementEntity;
import com.example.demo.enums.ReferenceTypeEnum;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovementEntity, Integer> {

        @EntityGraph(attributePaths = { "user", "inventoryItem", "inventoryItem.supplier" })
        @Query("SELECT i FROM InventoryMovementEntity i WHERE i.inventoryItem.id = :id")
        Page<InventoryMovementEntity> findAllByItemId(@Param("id") Integer id, Pageable pageable);

        @EntityGraph(attributePaths = { "user", "inventoryItem", "inventoryItem.supplier" })
        @Query("SELECT i FROM InventoryMovementEntity i WHERE i.referenceId = :id AND i.referenceType = :type")
        Page<InventoryMovementEntity> findAllByOrderId(@Param("id") Integer id, @Param("type") ReferenceTypeEnum orden,
                        Pageable pageable);

        @EntityGraph(attributePaths = { "user", "inventoryItem", "inventoryItem.supplier" })
        @Query("SELECT i FROM InventoryMovementEntity i WHERE i.user.id = :id")
        Page<InventoryMovementEntity> findAllByUserId(@Param("id") Integer id, Pageable pageable);

        @EntityGraph(attributePaths = { "user", "inventoryItem", "inventoryItem.supplier" })
        @Query("SELECT i FROM InventoryMovementEntity i WHERE i.inventoryItem.supplier.id = :id")
        Page<InventoryMovementEntity> findAllBySupplierId(@Param("id") Integer id, Pageable pageable);

        @EntityGraph(attributePaths = { "user", "inventoryItem", "inventoryItem.supplier" })
        @Query("SELECT i FROM InventoryMovementEntity i ORDER BY i.movementDate DESC")
        Page<InventoryMovementEntity> findAllLastMovements(Pageable pageable);

        @Query("""
                        SELECT im FROM InventoryMovementEntity im
                        JOIN FETCH im.user u
                        JOIN FETCH im.inventoryItem i
                        ORDER BY im.movementDate DESC
                        """)
        List<InventoryMovementEntity> findRecentMovements(Pageable pageable);

        @Query("""
                        SELECT im FROM InventoryMovementEntity im
                        WHERE im.movementDate BETWEEN :startDate AND :endDate
                        ORDER BY im.movementDate DESC
                        """)
        List<InventoryMovementEntity> findMovementsBetweenDates(
                        @Param("startDate") Instant startDate,
                        @Param("endDate") Instant endDate);

}
