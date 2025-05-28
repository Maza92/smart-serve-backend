package com.example.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.inventoryMovement.InventoryMovementDto;
import com.example.demo.entity.InventoryMovementEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface IInventoryMovementMapper {
    IInventoryMovementMapper INSTANCE = Mappers.getMapper(IInventoryMovementMapper.class);

    InventoryMovementDto toDto(InventoryMovementEntity entity);

}
