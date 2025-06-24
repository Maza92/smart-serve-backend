package com.example.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.inventoryMovement.InventoryMovementDto;
import com.example.demo.entity.InventoryMovementEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface IInventoryMovementMapper {
    IInventoryMovementMapper INSTANCE = Mappers.getMapper(IInventoryMovementMapper.class);

    @Mapping(target = "itemId", source = "inventoryItem.id")
    @Mapping(target = "itemImagePath", source = "inventoryItem.imagePath")
    @Mapping(target = "itemName", source = "inventoryItem.name")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "movementValue", ignore = true)
    InventoryMovementDto toDto(InventoryMovementEntity entity);

}
