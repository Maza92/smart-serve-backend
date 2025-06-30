package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.InventoryItemEntity;

import jakarta.validation.constraints.NotNull;

@Repository
public interface InventoryItemRepository
                extends JpaRepository<InventoryItemEntity, Integer>, JpaSpecificationExecutor<InventoryItemEntity> {

        @EntityGraph(attributePaths = { "supplier", "category", "unit" })
        @Query("SELECT i FROM InventoryItemEntity i")
        List<InventoryItemEntity> findAll();

        @EntityGraph(attributePaths = { "supplier", "category", "unit" })
        Page<InventoryItemEntity> findAll(@NotNull Specification<InventoryItemEntity> spec, Pageable pageable);

        @EntityGraph(attributePaths = { "supplier", "category", "unit" })
        @Query("SELECT i FROM InventoryItemEntity i WHERE i.id = :id")
        Optional<InventoryItemEntity> findById(@Param("id") int id);

        @EntityGraph(attributePaths = { "supplier", "unit" })
        @Query("select i from InventoryItemEntity i where i.supplier.id = :supplierId")
        Page<InventoryItemEntity> findAllBySupplierId(int supplierId, Pageable pageable);

        @Query("SELECT i FROM InventoryItemEntity i JOIN i.recipes r WHERE r.dish.id = :dishId")
        List<InventoryItemEntity> findItemsByDishId(int dishId);
}