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
import com.example.demo.dto.recipe.IngredientToDishDto;
import com.example.demo.dto.recipe.IngredientsSummaryDto;
import com.example.demo.dto.recipe.RecipeDto;
import com.example.demo.dto.recipe.RecipeSummaryDto;
import com.example.demo.dto.recipe.UpdateRecipeDto;
import com.example.demo.entity.RecipeEntity;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.dish.DishIngredientsDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        DateMapper.class, IInventoryItemMapper.class })
public interface IRecipeMapper {
    IRecipeMapper INSTANCE = Mappers.getMapper(IRecipeMapper.class);

    @Mapping(source = "inventoryItemId", target = "inventoryItem.id")
    RecipeEntity toEntity(CreateRecipeToDishDto dto);

    @Mapping(source = "inventoryItemId", target = "inventoryItem.id")
    @Mapping(source = "dishId", target = "dish.id")
    @Mapping(source = "unitId", target = "unit.id")
    RecipeEntity toCreateEntity(CreateRecipeDto dto);

    RecipeEntity updateEntityFromDto(UpdateRecipeDto dto, @MappingTarget RecipeEntity entity);

    RecipeDto toDto(RecipeEntity entity);

    List<RecipeDto> toDto(List<RecipeEntity> recipes);

    @Mapping(target = "name", source = "inventoryItem.name")
    @Mapping(target = "unitId", source = "inventoryItem.unit.id")
    @Mapping(target = "quantityRequired", source = "quantityRequired")
    RecipeSummaryDto toSummaryDto(RecipeEntity recipe);

    List<RecipeSummaryDto> toSummaryDto(Set<RecipeEntity> recipes);

    List<RecipeSummaryDto> toSummaryDto(List<RecipeEntity> recipes);

    @Mapping(target = "inventoryItemId", source = "inventoryItem.id")
    @Mapping(target = "recipeId", source = "id")
    IngredientsSummaryDto recipeEntityToIngredientsSummaryDto(RecipeEntity recipe);

    List<IngredientsSummaryDto> toIngredientsSummaryDto(List<RecipeEntity> recipes);

    @Mapping(target = "recipeId", source = "id")
    @Mapping(target = "inventoryItemId", source = "inventoryItem.id")
    @Mapping(target = "quantityRequired", source = "quantityRequired")
    @Mapping(target = "unitId", source = "unit.id")
    IngredientToDishDto toIngredientDto(RecipeEntity recipe);

    List<IngredientToDishDto> toIngredientsDto(List<RecipeEntity> recipes);

    @Mapping(target = "inventoryItemId", source = "inventoryItem.id")
    @Mapping(target = "name", source = "inventoryItem.name")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    @Mapping(target = "unitAbbreviation", source = "unit.abbreviation")
    @Mapping(target = "unitCost", source = "inventoryItem.unitCost")
    @Mapping(target = "quantityRequired", source = "quantityRequired")
    DishIngredientsDto toDishIngredientsDto(RecipeEntity recipe);

    default PageDto<RecipeDto> toPageDto(Page<RecipeEntity> recipePage) {
        List<RecipeDto> content = toDto(recipePage.getContent());

        return PageDto.fromPage(recipePage, content);
    }
}
