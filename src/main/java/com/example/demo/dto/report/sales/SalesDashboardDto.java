package com.example.demo.dto.report.sales;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDashboardDto {
    private SalesReportSummaryDto summary;
    private SalesChartDto salesOverview;
    private SalesChartDto topProducts;
    private SalesChartDto waiterPerformance;
    private SalesChartDto hourlySales;
    private SalesChartDto dailySales;
    private List<ProductSalesReportDto> topSellingProducts;
    private List<WaiterPerformanceDto> topWaiters;
}