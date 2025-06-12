package com.example.demo.mappers;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.recipe.CreateRecipeToDishDto;
import com.example.demo.dto.recipe.RecipeDto;
import com.example.demo.dto.recipe.RecipeSummaryDto;
import com.example.demo.entity.RecipeEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        DateMapper.class, IInventoryItemMapper.class })
public interface IRecipeMapper {
    IRecipeMapper INSTANCE = Mappers.getMapper(IRecipeMapper.class);

    @Mapping(source = "inventoryItemId", target = "inventoryItem.id")
    RecipeEntity toEntity(CreateRecipeToDishDto dto);

    RecipeDto toDto(RecipeEntity entity);

    @Mapping(target = "name", source = "inventoryItem.name")
    @Mapping(target = "unit", source = "inventoryItem.unit")
    @Mapping(target = "quantityRequired", source = "quantityRequired")
    RecipeSummaryDto toSummaryDto(RecipeEntity recipe);

    List<RecipeSummaryDto> toSummaryDto(Set<RecipeEntity> recipes);

    List<RecipeSummaryDto> toSummaryDto(List<RecipeEntity> recipes);
}
