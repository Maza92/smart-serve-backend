package com.example.demo.mappers;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.recipe.CreateRecipeDto;
import com.example.demo.dto.recipe.CreateRecipeToDishDto;
import com.example.demo.dto.recipe.IngredientsSummaryDto;
import com.example.demo.dto.recipe.RecipeDto;
import com.example.demo.dto.recipe.RecipeSummaryDto;
import com.example.demo.dto.recipe.UpdateRecipeDto;
import com.example.demo.entity.RecipeEntity;
import com.example.demo.dto.api.PageDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        DateMapper.class, IInventoryItemMapper.class })
public interface IRecipeMapper {
    IRecipeMapper INSTANCE = Mappers.getMapper(IRecipeMapper.class);

    @Mapping(source = "inventoryItemId", target = "inventoryItem.id")
    RecipeEntity toEntity(CreateRecipeToDishDto dto);

    @Mapping(source = "inventoryItemId", target = "inventoryItem.id")
    @Mapping(source = "dishId", target = "dish.id")
    RecipeEntity toCreateEntity(CreateRecipeDto dto);

    @Mapping(source = "inventoryItemId", target = "inventoryItem.id")
    RecipeEntity updateEntityFromDto(UpdateRecipeDto dto, @MappingTarget RecipeEntity entity);

    RecipeDto toDto(RecipeEntity entity);

    List<RecipeDto> toDto(List<RecipeEntity> recipes);

    @Mapping(target = "name", source = "inventoryItem.name")
    @Mapping(target = "unit", source = "inventoryItem.unit")
    @Mapping(target = "quantityRequired", source = "quantityRequired")
    RecipeSummaryDto toSummaryDto(RecipeEntity recipe);

    List<RecipeSummaryDto> toSummaryDto(Set<RecipeEntity> recipes);

    List<RecipeSummaryDto> toSummaryDto(List<RecipeEntity> recipes);

    @Mapping(target = "inventoryItemId", source = "inventoryItem.id")
    @Mapping(target = "recipeId", source = "id")
    IngredientsSummaryDto recipeEntityToIngredientsSummaryDto(RecipeEntity recipe);

    List<IngredientsSummaryDto> toIngredientsSummaryDto(List<RecipeEntity> recipes);

    default PageDto<RecipeDto> toPageDto(Page<RecipeEntity> recipePage) {
        List<RecipeDto> content = toDto(recipePage.getContent());

        return PageDto.fromPage(recipePage, content);
    }
}
