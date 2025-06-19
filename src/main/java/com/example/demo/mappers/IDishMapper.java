package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.dish.CreateDishDto;
import com.example.demo.dto.dish.DishDto;
import com.example.demo.dto.dish.DishWithIngredientsDto;
import com.example.demo.dto.dish.DishWithRecipesDto;
import com.example.demo.dto.dish.UpdateDishDto;
import com.example.demo.entity.DishEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        DateMapper.class, IRecipeMapper.class })
public interface IDishMapper {
    IDishMapper INSTANCE = Mappers.getMapper(IDishMapper.class);

    DishEntity toCreateEntity(CreateDishDto dto);

    @Mapping(target = "categoryId", source = "category.id")
    DishDto toDto(DishEntity entity);

    DishEntity updateEntityFromDto(UpdateDishDto dto, @MappingTarget DishEntity entity);

    @Mapping(target = "recipes", source = "recipes")
    DishWithRecipesDto toDtoWithRecipesSummary(DishEntity entity);

    List<DishWithRecipesDto> toDto(List<DishEntity> dishes);

    @Mapping(target = "categoryId", source = "category.id")
    DishWithIngredientsDto toDtoWithIngredients(DishEntity entity);

    default PageDto<DishWithRecipesDto> toPageDto(Page<DishEntity> dishPage) {
        List<DishWithRecipesDto> content = toDto(dishPage.getContent());

        return PageDto.fromPage(dishPage, content);
    }
}
