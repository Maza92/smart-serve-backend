package com.example.demo.dto.recipe;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeSummaryDto {
    private String name;
    private BigDecimal quantityRequired;
    private String unit;
}
