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
public class SalesTemporalTrendDto {
    private String period;
    private BigDecimal totalSales;
    private Long totalOrders;
    private BigDecimal averageTicket;
    private Integer dayOfWeek;
    private Integer hourOfDay;
    private Integer monthOfYear;
}