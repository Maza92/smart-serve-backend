package com.example.demo.dto.report.sales.projections;

import java.math.BigDecimal;

public interface WaiterPerformanceProjection {
    Long getWaiterId();

    String getFirstName();

    String getLastName();

    Long getTotalOrders();

    BigDecimal getTotalRevenue();

    BigDecimal getAverageTicket();

    String getProfileImagePath();
}
