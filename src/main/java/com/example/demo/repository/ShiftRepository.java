package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ShiftEntity;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Integer> {
}