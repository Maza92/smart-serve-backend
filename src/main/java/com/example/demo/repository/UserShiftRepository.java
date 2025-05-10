package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserShiftEntity;

@Repository
public interface UserShiftRepository extends JpaRepository<UserShiftEntity, Integer> {
}