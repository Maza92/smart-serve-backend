package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RoleEntity;
import com.example.demo.enums.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
	Optional<RoleEntity> findByName(RoleEnum role);
}
