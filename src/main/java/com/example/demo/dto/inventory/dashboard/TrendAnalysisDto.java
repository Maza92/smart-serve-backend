package com.example.demo.dto.inventory.dashboard;

import java.math.BigDecimal;
import java.util.Date;

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
public class TrendAnalysisDto {
    private Date month;
    private Integer totalItems;
    private BigDecimal totalValue;
    private Integer lowStockCount;
    private Integer totalMovements;
    private BigDecimal totalQuantityMoved;
}
