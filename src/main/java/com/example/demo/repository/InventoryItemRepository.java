package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.InventoryItemEntity;

@Repository
public interface InventoryItemRepository
                extends JpaRepository<InventoryItemEntity, Integer>, JpaSpecificationExecutor<InventoryItemEntity> {

        @EntityGraph(attributePaths = { "supplier" })
        @Query("select i from InventoryItemEntity i where i.supplier.id = :supplierId")
        Page<InventoryItemEntity> findAllBySupplierId(int supplierId, Pageable pageable);
}