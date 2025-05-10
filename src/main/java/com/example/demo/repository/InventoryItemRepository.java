package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.InventoryItemEntity;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Integer> {
}