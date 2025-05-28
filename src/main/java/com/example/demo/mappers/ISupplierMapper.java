package com.example.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.supplier.CreateSupplierDto;
import com.example.demo.dto.supplier.SupplierDto;
import com.example.demo.dto.supplier.UpdateSupplierDto;
import com.example.demo.entity.SupplierEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface ISupplierMapper {
    ISupplierMapper INSTANCE = Mappers.getMapper(ISupplierMapper.class);

    SupplierDto toDto(SupplierEntity entity);

    SupplierEntity toEntity(CreateSupplierDto dto);

    SupplierEntity updateEntityFromDto(UpdateSupplierDto dto, @MappingTarget SupplierEntity entity);
}