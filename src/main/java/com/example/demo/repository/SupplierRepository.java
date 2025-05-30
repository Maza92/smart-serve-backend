package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.SupplierEntity;

@Repository
public interface SupplierRepository
        extends JpaRepository<SupplierEntity, Integer>, JpaSpecificationExecutor<SupplierEntity> {
}