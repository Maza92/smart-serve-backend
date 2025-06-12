package com.example.demo.dto.dish;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.example.demo.dto.recipe.RecipeDto;

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
public class DishDto {
    Integer id;
    String name;
    String description;
    BigDecimal basePrice;
    String category;
    String imageUrl;
    BigDecimal preparationTime;
    Boolean isActive;
    Boolean isFeatured;
    String createdAt;
    String updatedAt;
}