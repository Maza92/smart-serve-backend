package com.example.demo.dto.report.sales;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportSummaryDto {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private BigDecimal averageTicket;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal totalCost;
    private BigDecimal totalMargin;
    private BigDecimal marginPercentage;
    private Integer totalCustomers;
    private LocalDate periodStart;
    private LocalDate periodEnd;
}
