package com.example.demo.dto.dish;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.dto.recipe.RecipeSummaryDto;

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
public class DishWithRecipesDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal preparationTime;
    private Boolean isFeatured;
    private List<RecipeSummaryDto> recipes;
}
