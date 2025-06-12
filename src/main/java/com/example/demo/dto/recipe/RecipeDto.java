package com.example.demo.dto.recipe;

import java.math.BigDecimal;

import com.example.demo.dto.inventoryItem.InventoryItemDto;

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
public class RecipeDto {
    Integer id;
    InventoryItemDto inventoryItem;
    BigDecimal quantityRequired;
    String notes;
    Integer preparationOrder;
}