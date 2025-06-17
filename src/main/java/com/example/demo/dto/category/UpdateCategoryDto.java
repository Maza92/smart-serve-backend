package com.example.demo.dto.category;

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
public class UpdateCategoryDto {

    @NotBlank(message = "{validation.category.name.notBlank}")
    @Size(min = 2, max = 50, message = "{validation.category.name.size}")
    private String name;

    @NotBlank(message = "{validation.category.description.notBlank}")
    @Size(min = 2, max = 255, message = "{validation.category.description.size}")
    private String description;

    @NotBlank(message = "{validation.category.isActive.notBlank}")
    private Boolean isActive;
}
