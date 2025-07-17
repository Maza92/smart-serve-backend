package com.example.demo.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.dish.DishIngredientsDto;
import com.example.demo.dto.inventoryItem.CreateInventoryItemDto;
import com.example.demo.dto.inventoryItem.InventoryItemDto;
import com.example.demo.dto.inventoryItem.UpdateInventoryItemDto;
import com.example.demo.entity.InventoryItemEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface IInventoryItemMapper {
    IInventoryItemMapper INSTANCE = Mappers.getMapper(IInventoryItemMapper.class);

    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    @Mapping(target = "unitAbbreviation", source = "unit.abbreviation")
    InventoryItemDto toDto(InventoryItemEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "unit", ignore = true)
    InventoryItemEntity toEntity(CreateInventoryItemDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "unit", ignore = true)
    InventoryItemEntity updateEntityFromDto(UpdateInventoryItemDto dto, @MappingTarget InventoryItemEntity entity);

    @Mapping(target = "inventoryItemId", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "unitId", source = "unit.id")
    DishIngredientsDto toDishIngredientsDto(InventoryItemEntity entity);
}