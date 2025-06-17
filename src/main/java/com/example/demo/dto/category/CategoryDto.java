package com.example.demo.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;
}
