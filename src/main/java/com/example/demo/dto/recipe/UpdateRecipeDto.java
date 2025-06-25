package com.example.demo.dto.recipe;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class UpdateRecipeDto {
    @NotNull(message = "{validation.recipe.inventoryItemId.notNull}")
    Integer inventoryItemId;

    @NotNull(message = "{validation.recipe.quantityRequired.notNull}")
    @Positive(message = "{validation.recipe.quantityRequired.positive}")
    BigDecimal quantityRequired;

    @Size(max = 500, message = "{validation.recipe.notes.size}")
    String notes;

    @NotNull(message = "{validation.recipe.preparationOrder.notNull}")
    @Positive(message = "{validation.recipe.preparationOrder.positive}")
    Integer preparationOrder;
}