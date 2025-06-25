package com.example.demo.dto.dish;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.dto.recipe.CreateRecipeToDishDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateDishDto {
    @NotBlank(message = "{validation.dish.name.notBlank}")
    @Size(min = 2, max = 100, message = "{validation.dish.name.size}")
    String name;

    @NotBlank(message = "{validation.dish.description.notBlank}")
    @Size(max = 500, message = "{validation.dish.description.size}")
    String description;

    @NotNull(message = "{validation.dish.basePrice.notNull}")
    @Positive(message = "{validation.dish.basePrice.positive}")
    Double basePrice;

    @NotNull(message = "{validation.dish.categoryId.notNull}")
    Integer categoryId;

    @Size(max = 255, message = "{validation.dish.imageUrl.size}")
    String imageUrl;

    @NotNull(message = "{validation.dish.preparationTime.notNull}")
    @Positive(message = "{validation.dish.preparationTime.positive}")
    BigDecimal preparationTime;

    Boolean isActive;

    Boolean isFeatured;
}