package com.example.demo.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.cashMovement.CashMovementDto;
import com.example.demo.dto.cashMovement.CreateCashMovementDto;
import com.example.demo.entity.CashMovementEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface ICashMovementMapper {
    ICashMovementMapper INSTANCE = Mappers.getMapper(ICashMovementMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cashRegister.id", source = "cashRegisterId")
    @Mapping(target = "movementType", source = "movementType")
    @Mapping(target = "movementDate", ignore = true)
    @Mapping(target = "active", constant = "true")
    CashMovementEntity toEntity(CreateCashMovementDto dto);

    @Mapping(target = "cashRegisterId", source = "cashRegister.id")
    @Mapping(target = "cashRegisterStatus", source = "cashRegister.status")
    @Mapping(target = "username", source = "user.username")
    CashMovementDto toDto(CashMovementEntity entity);
}