package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UnitEntity;
import com.example.demo.enums.UnitTypeEnum;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {
    Optional<UnitEntity> findByAbbreviation(String abbreviation);

    List<UnitEntity> findByUnitType(UnitTypeEnum unitType);

    @Query("SELECT u FROM UnitEntity u WHERE u.isBaseUnit = :isBaseUnit")
    List<UnitEntity> findAllByIsBaseUnit(@Param("isBaseUnit") Boolean isBaseUnit);

    Optional<UnitEntity> findByUnitTypeAndIsBaseUnit(UnitTypeEnum unitType, Boolean isBaseUnit);
}
