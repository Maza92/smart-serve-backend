package com.example.demo.dto.category;

import com.example.demo.annotation.ImportColumn;
import com.example.demo.enums.CategoryEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryImportDto {
    @ImportColumn(name = "Category Name", required = true)
    private String name;

    @ImportColumn(name = "Category Description", required = true)
    private String description;

    @ImportColumn(name = "Category Type", required = true)
    private CategoryEnum categoryType;

    @ImportColumn(name = "Is Active", required = true)
    private Boolean isActive;
}
