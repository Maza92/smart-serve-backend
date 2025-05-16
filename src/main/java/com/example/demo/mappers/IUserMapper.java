package com.example.demo.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    @Mapping(target = "roleName", source = "role.name")
    UserDto toDto(UserEntity entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity updateEntityFromDto(UserDto dto, @MappingTarget UserEntity entity);
}