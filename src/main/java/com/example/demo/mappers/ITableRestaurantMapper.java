package com.example.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.restaurantTable.*;
import com.example.demo.entity.RestaurantTableEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface ITableRestaurantMapper {
    ITableRestaurantMapper INSTANCE = Mappers.getMapper(ITableRestaurantMapper.class);

    void update(@MappingTarget RestaurantTableEntity entity, UpdateRestaurantTableDto dto);

    RestaurantTableDto toDto(RestaurantTableEntity entity);
}
