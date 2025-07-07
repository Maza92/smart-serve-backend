package com.example.demo.dto.report.sales.projections;

import java.math.BigDecimal;

public interface ProductSalesProjection {
    Long getDishId();

    String getDishName();

    String getCategoryName();

    Long getQuantitySold();

    BigDecimal getTotalRevenue();

    BigDecimal getTotalCost();

    String getImageUrl();
}
