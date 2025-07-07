package com.example.demo.dto.inventory.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
public class CategoryStockLevelDto {
    private String categoryName;
    private Double stockPercentage;
    private Long totalItems;
    private Long lowStockItems;
    private BigDecimal totalValue;

    public CategoryStockLevelDto(String categoryName, Double stockPercentage, Long totalItems, Long lowStockItems,
            BigDecimal totalValue) {
        this.categoryName = categoryName;
        this.stockPercentage = stockPercentage;
        this.totalItems = totalItems;
        this.lowStockItems = lowStockItems;
        this.totalValue = totalValue;
    }
}
