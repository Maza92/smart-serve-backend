package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RestaurantTableEntity;
import com.example.demo.enums.RestaurantTableEnum;

@Repository
public interface RestaurantTableRepository
        extends JpaRepository<RestaurantTableEntity, Integer>, JpaSpecificationExecutor<RestaurantTableEntity> {

    @Query("SELECT r.status FROM RestaurantTableEntity r WHERE r.id = :id")
    Optional<RestaurantTableEnum> findByStatus(@Param("id") Integer id);
}