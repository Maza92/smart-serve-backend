package com.example.demo.dto.inventory.dashboard;

import java.math.BigDecimal;

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
public class LowStockItemDto {
    private Integer itemId;
    private String itemName;
    private BigDecimal currentStock;
    private BigDecimal minStockLevel;
    private String unitName;
    private String categoryName;
}
