package com.example.demo.dto.report.sales;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesReportDto {
    private Long dishId;
    private String dishName;
    private String categoryName;
    private Long quantitySold;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal margin;
    private BigDecimal marginPercentage;
    private String imageUrl;
}
