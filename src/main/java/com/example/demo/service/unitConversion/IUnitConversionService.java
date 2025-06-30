package com.example.demo.service.unitConversion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.unit.UnitDto;
import com.example.demo.entity.UnitEntity;
import com.example.demo.enums.UnitTypeEnum;

public interface IUnitConversionService {
    BigDecimal convertUnit(BigDecimal quantity, Integer fromUnitId, Integer toUnitId);

    List<UnitEntity> getUnitsByType(UnitTypeEnum unitType);

    ApiSuccessDto<List<UnitDto>> getUnits();

    Optional<UnitEntity> findByAbbreviation(String abbreviation);
}
