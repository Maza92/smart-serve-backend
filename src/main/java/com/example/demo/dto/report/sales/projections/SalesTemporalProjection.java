package com.example.demo.dto.report.sales.projections;

import java.math.BigDecimal;

public interface SalesTemporalProjection {
    String getPeriod();

    BigDecimal getTotalSales();

    Long getTotalOrders();

    BigDecimal getAverageTicket();

    Integer getDayOfWeek();

    Integer getHourOfDay();

    Integer getMonthOfYear();
}
