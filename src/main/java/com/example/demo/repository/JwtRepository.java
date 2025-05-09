package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.JwtTokenEntity;

@Repository
public interface JwtRepository extends JpaRepository<JwtTokenEntity, Integer> {
	@Query("SELECT COUNT(j) > 0 FROM JwtTokenEntity j WHERE j.token = :token")
	boolean existsByToken(@Param("token") String token);

	@Query("SELECT COUNT(j) > 0 FROM JwtTokenEntity j WHERE j.token = :token AND j.isRevoked = true")
	boolean isRevoked(@Param("token") String token);

	@Query("SELECT COUNT(j) > 0 FROM JwtTokenEntity j WHERE j.token = :token AND j.isValid = true")
	boolean isValid(@Param("token") String token);
}
