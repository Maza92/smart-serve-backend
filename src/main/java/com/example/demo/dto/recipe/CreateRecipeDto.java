package com.example.demo.dto.recipe;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CreateRecipeDto extends CreateRecipeToDishDto {
    @NotNull(message = "{validation.recipe.dishId.notNull}")
    Integer dishId;
}
