package com.example.demo.dto.recipe;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class IngredientsSummaryDto {
    Integer recipeId;
    Integer inventoryItemId;
    BigDecimal quantityRequired;
    String notes;
    Integer preparationOrder;
}
