package com.example.demo.controller.report.impl;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.report.IReportController;
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
import com.example.demo.service.report.IReportService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController implements IReportController {
        private final IReportService reportService;
        private final MessageUtils messageUtil;

        @Override
        public ResponseEntity<ApiSuccessDto<ReportDashboardDto>> getDashboardData(LocalDate startDate,
                        LocalDate endDate) {
                return ResponseEntity.ok(reportService.getDashboardData(startDate, endDate));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<SalesOverviewDto>> getSalesOverview(LocalDate startDate,
                        LocalDate endDate) {
                return ResponseEntity.ok(
                                ApiSuccessDto.of(HttpStatus.OK.value(),
                                                messageUtil.getMessage("operation.report.sales-overview.success"),
                                                reportService.getSalesOverview(startDate, endDate)));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<SalesSummaryDto>> getSalesSummary(LocalDate startDate, LocalDate endDate) {
                return ResponseEntity.ok(ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtil.getMessage("operation.report.sales-summary.success"),
                                reportService.getSalesSummary(startDate, endDate)));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<DishPerformanceDto>> getDishPerformance(LocalDate startDate,
                        LocalDate endDate,
                        Integer limit) {
                return ResponseEntity.ok(
                                ApiSuccessDto.of(HttpStatus.OK.value(),
                                                messageUtil.getMessage("operation.report.dish-performance.success"),
                                                reportService.getDishPerformance(startDate, endDate,
                                                                limit)));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<PaymentMethodDistributionDto>> getPaymentMethodDistribution(
                        LocalDate startDate,
                        LocalDate endDate) {
                return ResponseEntity.ok(ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtil.getMessage("operation.report.payment-method-distribution.success"),
                                reportService.getPaymentMethodDistribution(startDate, endDate)));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<PagedSalesReportDto<ProductSalesReportDto>>> getProductSalesReport(
                        SalesReportFiltersDto filters, int page, int size) {
                return ResponseEntity.ok(
                                reportService.getProductSalesReport(filters, page, size));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<PagedSalesReportDto<WaiterPerformanceDto>>> getWaiterPerformanceReport(
                        SalesReportFiltersDto filters, int page, int size) {
                return ResponseEntity.ok(
                                reportService.getWaiterPerformanceReport(filters, page, size));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<SalesChartDto>> getSalesTemporalTrends(
                        SalesReportFiltersDto filters) {
                return ResponseEntity.ok(
                                reportService.getSalesTemporalTrends(filters));
        }

        @Override
        public ResponseEntity<ApiSuccessDto<List<WaiterPerformanceDto>>> getTopWaiters(
                        SalesReportFiltersDto filters, int limit) {
                return ResponseEntity.ok(
                                reportService.getTopWaiters(filters, limit));
        }

}
