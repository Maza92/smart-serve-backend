package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DishEntity;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Integer>, JpaSpecificationExecutor<DishEntity> {

    @EntityGraph(attributePaths = { "recipes", "recipes.inventoryItem" })
    Page<DishEntity> findAll(Specification<DishEntity> spec, Pageable pageable);
}