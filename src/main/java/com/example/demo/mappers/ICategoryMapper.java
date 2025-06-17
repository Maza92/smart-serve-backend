package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CategoryImportDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.example.demo.dto.category.UpdateCategoryDto;
import com.example.demo.entity.CategoryEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        DateMapper.class, IRecipeMapper.class })
public interface ICategoryMapper {
    CategoryEntity toCreateEntity(CreateCategoryDto dto);

    CategoryEntity toImportEntity(CategoryImportDto dto);

    CategoryDto toDto(CategoryEntity entity);

    CategoryEntity updateEntityFromDto(UpdateCategoryDto dto, @MappingTarget CategoryEntity entity);

    List<CategoryDto> toDto(List<CategoryEntity> categories);

    default PageDto<CategoryDto> toPageDto(Page<CategoryEntity> categoryPage) {
        List<CategoryDto> content = toDto(categoryPage.getContent());

        return PageDto.fromPage(categoryPage, content);
    }
}