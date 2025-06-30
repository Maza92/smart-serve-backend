package com.example.demo.service.unitConversion.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.unit.UnitDto;
import com.example.demo.entity.ConversionFactorEntity;
import com.example.demo.entity.UnitEntity;
import com.example.demo.enums.UnitTypeEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IUnitMapper;
import com.example.demo.repository.ConversionFactorRepository;
import com.example.demo.repository.UnitRepository;
import com.example.demo.service.unitConversion.IUnitConversionService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnitConversionService implements IUnitConversionService {
    private final UnitRepository unitRepository;
    private final ConversionFactorRepository conversionFactorRepository;
    private final ApiExceptionFactory apiExceptionFactory;
    private final MessageUtils messageUtils;
    private final IUnitMapper unitMapper;

    public BigDecimal convertUnit(BigDecimal quantity, Integer fromUnitId, Integer toUnitId) {
        if (fromUnitId.equals(toUnitId)) {
            return quantity;
        }

        UnitEntity fromUnit = unitRepository.findById(fromUnitId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.unit.conversion.from.unit.not.found"));

        UnitEntity toUnit = unitRepository.findById(toUnitId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.unit.conversion.to.unit.not.found"));

        if (fromUnit.getUnitType() != toUnit.getUnitType()) {
            throw apiExceptionFactory.badRequestException(
                    "operation.unit.conversion.invalid.unit.type",
                    fromUnit.getUnitType(), toUnit.getUnitType());
        }

        BigDecimal quantityInBaseUnit = convertToBaseUnit(quantity, fromUnit);
        return convertFromBaseUnit(quantityInBaseUnit, toUnit);
    }

    private BigDecimal convertToBaseUnit(BigDecimal quantity, UnitEntity fromUnit) {
        if (fromUnit.getIsBaseUnit()) {
            return quantity;
        }

        ConversionFactorEntity factor = conversionFactorRepository.findByFromUnitId(fromUnit.getId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound(
                        "operation.unit.conversion.conversion.factor.not.found"));

        return quantity.multiply(factor.getFactor());
    }

    private BigDecimal convertFromBaseUnit(BigDecimal quantityInBaseUnit, UnitEntity toUnit) {
        if (toUnit.getIsBaseUnit()) {
            return quantityInBaseUnit;
        }

        ConversionFactorEntity factor = conversionFactorRepository.findByFromUnitId(toUnit.getId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound(
                        "operation.unit.conversion.conversion.factor.not.found", toUnit.getAbbreviation()));

        return quantityInBaseUnit.divide(factor.getFactor(), 8, RoundingMode.HALF_UP);
    }

    public List<UnitEntity> getUnitsByType(UnitTypeEnum unitType) {
        return unitRepository.findByUnitType(unitType);
    }

    public ApiSuccessDto<List<UnitDto>> getUnits() {
        List<UnitEntity> units = unitRepository.findAll();
        List<UnitDto> dtos = unitMapper.toDto(units);
        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.unit.get.success"), dtos);
    }

    public Optional<UnitEntity> findByAbbreviation(String abbreviation) {
        return unitRepository.findByAbbreviation(abbreviation);
    }
}
