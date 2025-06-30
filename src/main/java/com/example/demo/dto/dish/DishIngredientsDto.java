package com.example.demo.dto.dish;

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
public class DishIngredientsDto {
    private Integer inventoryItemId;
    private String name;
    private Integer quantityRequired;
    private Integer unitId;
    private String unitName;
    private String unitAbbreviation;
    private String unitCost;
}
