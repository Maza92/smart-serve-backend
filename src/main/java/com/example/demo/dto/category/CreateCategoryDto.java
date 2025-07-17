package com.example.demo.dto.category;

import com.example.demo.enums.CategoryEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
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
public class CreateCategoryDto {

    @NotBlank(message = "{validation.category.name.notBlank}")
    @Size(min = 2, max = 50, message = "{validation.category.name.size}")
    private String name;

    @NotBlank(message = "{validation.category.description.notBlank}")
    @Size(min = 2, max = 255, message = "{validation.category.description.size}")
    private String description;

    @NotBlank(message = "{validation.category.categoryType.notBlank}")
    @Size(min = 2, max = 50, message = "{validation.category.categoryType.size}")
    @Enumerated(EnumType.STRING)
    private CategoryEnum categoryType;

    @NotBlank(message = "{validation.category.isActive.notBlank}")
    private Boolean isActive;
}
