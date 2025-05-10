package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DishEntity;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Integer> {
}