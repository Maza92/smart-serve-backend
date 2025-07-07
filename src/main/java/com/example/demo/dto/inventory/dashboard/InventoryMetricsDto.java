package com.example.demo.dto.inventory.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMetricsDto {
    private TotalItemsMetricDto totalItems;
    private LowStockMetricDto lowStock;
    private TotalValueMetricDto totalValue;
    private CategoriesMetricDto categories;
}
