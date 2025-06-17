package com.example.demo.dto.category;

import com.example.demo.annotation.ImportColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryImportDto {
    @ImportColumn(name = "Category Name", required = true)
    private String name;

    @ImportColumn(name = "Category Description", required = true)
    private String description;

    @ImportColumn(name = "Is Active", required = true)
    private Boolean isActive;
}
