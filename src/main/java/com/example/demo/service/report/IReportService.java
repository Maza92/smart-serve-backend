package com.example.demo.service.report;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.report.dashboard.DishPerformanceDto;
import com.example.demo.dto.report.dashboard.PaymentMethodDistributionDto;
import com.example.demo.dto.report.dashboard.ReportDashboardDto;
import com.example.demo.dto.report.dashboard.SalesOverviewDto;
import com.example.demo.dto.report.dashboard.SalesSummaryDto;
import com.example.demo.dto.report.sales.PagedSalesReportDto;
import com.example.demo.dto.report.sales.ProductSalesReportDto;
import com.example.demo.dto.report.sales.SalesChartDto;
import com.example.demo.dto.report.sales.SalesReportFiltersDto;
import com.example.demo.dto.report.sales.WaiterPerformanceDto;

public interface IReportService {

        ApiSuccessDto<ReportDashboardDto> getDashboardData(LocalDate startDate, LocalDate endDate);

        SalesOverviewDto getSalesOverview(LocalDate startDate, LocalDate endDate);

        SalesSummaryDto getSalesSummary(LocalDate startDate, LocalDate endDate);

        DishPerformanceDto getDishPerformance(LocalDate startDate, LocalDate endDate, Integer limit);

        DishPerformanceDto getAllDishPerformance(LocalDate startDate, LocalDate endDate);

        PaymentMethodDistributionDto getPaymentMethodDistribution(LocalDate startDate, LocalDate endDate);

        ApiSuccessDto<PagedSalesReportDto<ProductSalesReportDto>> getProductSalesReport(
                        SalesReportFiltersDto filters, int page, int size);

        ApiSuccessDto<PagedSalesReportDto<WaiterPerformanceDto>> getWaiterPerformanceReport(
                        SalesReportFiltersDto filters, int page, int size);

        ApiSuccessDto<List<WaiterPerformanceDto>> getTopWaiters(SalesReportFiltersDto filters, int limit);

        ApiSuccessDto<SalesChartDto> getSalesTemporalTrends(SalesReportFiltersDto filters);
}
