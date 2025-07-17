package com.example.demo.dto.report.dashboard;

import java.time.Instant;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDashboardDto {
    private SalesOverviewDto salesOverview;
    private SalesSummaryDto salesSummary;
    private DishPerformanceDto dishPerformance;
    private PaymentMethodDistributionDto paymentMethodDistribution;
    private LocalDate startDate;
    private LocalDate endDate;
}
