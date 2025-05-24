package com.example.demo.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.cashRegister.CashRegisterDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;
import com.example.demo.entity.CashRegisterEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface ICashRegisterMapper {
    ICashRegisterMapper INSTANCE = Mappers.getMapper(ICashRegisterMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "openDate", ignore = true)
    @Mapping(target = "closeDate", ignore = true)
    @Mapping(target = "finalAmount", ignore = true)
    @Mapping(target = "expectedAmount", ignore = true)
    @Mapping(target = "difference", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "cashMovements", ignore = true)
    void updateToOpen(@MappingTarget CashRegisterEntity entity, OpenCashRegisterDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "openDate", ignore = true)
    @Mapping(target = "closeDate", ignore = true)
    @Mapping(target = "initialAmount", ignore = true)
    @Mapping(target = "expectedAmount", ignore = true)
    @Mapping(target = "difference", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "cashMovements", ignore = true)
    void updateToClose(@MappingTarget CashRegisterEntity entity, ClosedCashRegisterDto dto);

    @Mapping(target = "user", source = "user.username")
    @Mapping(target = "createdAt", source = "createdAt")
    CashRegisterDto toDto(CashRegisterEntity entity);

}
