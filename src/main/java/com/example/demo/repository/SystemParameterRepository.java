package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.SystemParameterEntity;

public interface SystemParameterRepository extends JpaRepository<SystemParameterEntity, Integer> {
    Optional<SystemParameterEntity> findByParamKey(String paramKey);
}
