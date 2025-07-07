package com.example.demo.service.report.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.report.dashboard.DailySalesDto;
import com.example.demo.dto.report.dashboard.DishDataPerformanceDto;
import com.example.demo.dto.report.dashboard.DishPerformanceDto;
import com.example.demo.dto.report.dashboard.PaymentMethodDataDto;
import com.example.demo.dto.report.dashboard.PaymentMethodDistributionDto;
import com.example.demo.dto.report.dashboard.ReportDashboardDto;
import com.example.demo.dto.report.dashboard.SalesOverviewDto;
import com.example.demo.dto.report.dashboard.SalesSummaryDto;
import com.example.demo.dto.report.sales.PagedSalesReportDto;
import com.example.demo.dto.report.sales.ProductSalesReportDto;
import com.example.demo.dto.report.sales.SalesChartDto;
import com.example.demo.dto.report.sales.SalesReportFiltersDto;
import com.example.demo.dto.report.sales.SalesReportSummaryDto;
import com.example.demo.dto.report.sales.WaiterPerformanceDto;
import com.example.demo.dto.report.sales.projections.ProductSalesProjection;
import com.example.demo.dto.report.sales.projections.SalesTemporalProjection;
import com.example.demo.dto.report.sales.projections.WaiterPerformanceProjection;
import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.report.IReportService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements IReportService {

        private final DishRepository dishRepository;
        private final OrderRepository orderRepository;
        private final TransactionRepository transactionRepository;
        private final ApiExceptionFactory apiExceptionFactory;
        private final MessageUtils messageUtil;

        public ApiSuccessDto<ReportDashboardDto> getDashboardData(LocalDate startDate, LocalDate endDate) {

                ReportDashboardDto dashboard = ReportDashboardDto.builder()
                                .salesOverview(getSalesOverview(startDate, endDate))
                                .salesSummary(getSalesSummary(startDate, endDate))
                                .dishPerformance(getDishPerformance(startDate, endDate, 5))
                                .paymentMethodDistribution(getPaymentMethodDistribution(startDate, endDate))
                                .startDate(startDate)
                                .endDate(endDate)
                                .build();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtil.getMessage("operation.report.dashboard.success"),
                                dashboard);
        }

        public SalesOverviewDto getSalesOverview(LocalDate startDate, LocalDate endDate) {
                Pair<Instant, Instant> dates = getDateRange(startDate, endDate);
                List<DailySalesDto> dailySales = orderRepository.findDailySales(dates.getLeft(), dates.getRight());

                Map<LocalDate, BigDecimal> salesMap = dailySales.stream()
                                .collect(Collectors.toMap(
                                                dto -> dto.getDate().atZone(ZoneOffset.UTC).toLocalDate(),
                                                DailySalesDto::getTotalSales,
                                                (existing, replacement) -> existing));

                List<LocalDate> dateRange = generateDateRange(startDate, endDate);

                List<String> labels = dateRange.stream()
                                .map(date -> date.getDayOfWeek().getDisplayName(TextStyle.SHORT,
                                                LocaleContextHolder.getLocale()))
                                .collect(Collectors.toList());

                List<BigDecimal> values = dateRange.stream()
                                .map(date -> salesMap.getOrDefault(date, BigDecimal.ZERO))
                                .collect(Collectors.toList());

                BigDecimal minValue = values.stream()
                                .min(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO);

                BigDecimal maxValue = values.stream()
                                .max(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO);

                return SalesOverviewDto.builder()
                                .labels(labels)
                                .values(values)
                                .minValue(minValue)
                                .maxValue(maxValue)
                                .build();
        }

        public SalesSummaryDto getSalesSummary(LocalDate startDate, LocalDate endDate) {
                Pair<Instant, Instant> dates = getDateRange(startDate, endDate);
                Long totalTransactions = transactionRepository.countTransactionsByDateRange(dates.getLeft(),
                                dates.getRight());
                BigDecimal totalRevenue = orderRepository.findTotalRevenueByDateRange(dates.getLeft(),
                                dates.getRight());
                Long totalOrders = orderRepository.countOrdersByDateRange(dates.getLeft(), dates.getRight());

                BigDecimal averageTicket = BigDecimal.ZERO;
                if (totalOrders > 0) {
                        averageTicket = totalRevenue.divide(
                                        BigDecimal.valueOf(totalOrders),
                                        2,
                                        RoundingMode.HALF_UP);
                }

                return SalesSummaryDto.builder()
                                .totalTransactions(totalTransactions)
                                .totalRevenue(totalRevenue)
                                .averageTicket(averageTicket)
                                .build();
        }

        public DishPerformanceDto getAllDishPerformance(LocalDate startDate, LocalDate endDate) {
                return getDishPerformance(startDate, endDate, null);
        }

        public PaymentMethodDistributionDto getPaymentMethodDistribution(LocalDate startDate,
                        LocalDate endDate) {
                Pair<Instant, Instant> dates = getDateRange(startDate, endDate);
                List<PaymentMethodDataDto> paymentData = transactionRepository
                                .findPaymentMethodDistribution(dates.getLeft(), dates.getRight());

                BigDecimal totalAmount = paymentData.stream()
                                .map(PaymentMethodDataDto::getTotalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                List<PaymentMethodEnum> paymentMethods = paymentData.stream()
                                .map(PaymentMethodDataDto::getPaymentMethod)
                                .collect(Collectors.toList());

                List<BigDecimal> percentages = paymentData.stream()
                                .map(projection -> {
                                        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                                                return projection.getTotalAmount()
                                                                .divide(totalAmount, 4, RoundingMode.HALF_UP)
                                                                .multiply(BigDecimal.valueOf(100));
                                        }
                                        return BigDecimal.ZERO;
                                })
                                .collect(Collectors.toList());

                return PaymentMethodDistributionDto.builder()
                                .paymentMethods(paymentMethods)
                                .percentages(percentages)
                                .totalAmount(totalAmount)
                                .build();
        }

        public DishPerformanceDto getDishPerformance(LocalDate startDate, LocalDate endDate, Integer limit) {
                Pageable pageable = limit != null ? PageRequest.of(0, limit) : Pageable.unpaged();

                Pair<Instant, Instant> dates = getDateRange(startDate, endDate);
                List<DishDataPerformanceDto> dishPerformance = dishRepository
                                .findDishPerformance(dates.getLeft(), dates.getRight(), pageable);

                List<String> dishNames = dishPerformance.stream()
                                .map(DishDataPerformanceDto::getDishName)
                                .collect(Collectors.toList());

                List<Integer> quantities = dishPerformance.stream()
                                .map(projection -> projection.getTotalQuantity().intValue())
                                .collect(Collectors.toList());

                return DishPerformanceDto.builder()
                                .dishNames(dishNames)
                                .quantities(quantities)
                                .totalDishesAnalyzed(dishPerformance.size())
                                .isLimited(limit != null && dishPerformance.size() == limit)
                                .build();
        }

        @Override
        public ApiSuccessDto<PagedSalesReportDto<ProductSalesReportDto>> getProductSalesReport(
                        SalesReportFiltersDto filters, int page, int size) {

                if (size <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

                if (page <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

                Pair<Instant, Instant> dates = getDateRange(filters.getStartDate(), filters.getEndDate());

                Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
                Page<ProductSalesProjection> projections = orderRepository.findProductSalesReport(
                                dates.getLeft(), dates.getRight(), filters.getCategoryId(), filters.getOrderStatus(),
                                pageable);

                List<ProductSalesReportDto> content = projections.getContent().stream()
                                .map(this::mapToProductSalesReportDto)
                                .toList();

                PagedSalesReportDto<ProductSalesReportDto> pagedSalesReportDto = PagedSalesReportDto
                                .<ProductSalesReportDto>builder()
                                .content(PageDto.fromPage(projections, content))
                                .summary(getSalesSummary(filters))
                                .build();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtil.getMessage("operation.report.sales.success"),
                                pagedSalesReportDto);
        }

        @Override
        public ApiSuccessDto<PagedSalesReportDto<WaiterPerformanceDto>> getWaiterPerformanceReport(
                        SalesReportFiltersDto filters, int page, int size) {
                if (size <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

                if (page <= 0)
                        throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

                Pair<Instant, Instant> dates = getDateRange(filters.getStartDate(), filters.getEndDate());

                Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
                Page<WaiterPerformanceProjection> projections = orderRepository.findGeneralWaiterPerformanceReport(
                                dates.getLeft(), dates.getRight(),
                                filters.getOrderStatus(),
                                pageable);

                BigDecimal totalRevenue = projections.getContent().stream()
                                .map(WaiterPerformanceProjection::getTotalRevenue)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                List<WaiterPerformanceDto> content = projections.getContent().stream()
                                .map(projection -> mapWaiterPerformanceProjection(projection, totalRevenue))
                                .collect(Collectors.toList());

                PagedSalesReportDto<WaiterPerformanceDto> pagedSalesReportDto = PagedSalesReportDto
                                .<WaiterPerformanceDto>builder()
                                .content(PageDto.fromPage(projections, content))
                                .summary(getSalesSummary(filters))
                                .build();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtil.getMessage("operation.report.sales.success"),
                                pagedSalesReportDto);
        }

        @Override
        public ApiSuccessDto<List<WaiterPerformanceDto>> getTopWaiters(SalesReportFiltersDto filters, int limit) {
                Pair<Instant, Instant> dates = getDateRange(filters.getStartDate(), filters.getEndDate());

                List<WaiterPerformanceProjection> projections = orderRepository.findTopWaiters(
                                dates.getLeft(), dates.getRight(), limit);

                List<WaiterPerformanceDto> content = projections.stream()
                                .map(projection -> mapWaiterPerformanceProjection(projection, BigDecimal.ZERO))
                                .collect(Collectors.toList());

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtil.getMessage("operation.report.sales.success"),
                                content);
        }

        @Override
        public ApiSuccessDto<SalesChartDto> getSalesTemporalTrends(SalesReportFiltersDto filters) {
                Pair<Instant, Instant> dates = getDateRange(filters.getStartDate(), filters.getEndDate());

                List<SalesTemporalProjection> projections = switch (filters.getTemporalGrouping()) {
                        case "DAY_OF_WEEK" -> orderRepository.findSalesByDayOfWeek(
                                        dates.getLeft(), dates.getRight(), filters.getOrderStatus());
                        case "HOUR" -> orderRepository.findSalesByHour(
                                        dates.getLeft(), dates.getRight(), filters.getOrderStatus());
                        case "MONTH" -> orderRepository.findSalesByMonth(
                                        dates.getLeft(), dates.getRight(), filters.getOrderStatus());
                        default -> orderRepository.findSalesByDay(
                                        dates.getLeft(), dates.getRight(), filters.getOrderStatus());
                };

                List<String> labels = projections.stream()
                                .map(SalesTemporalProjection::getPeriod)
                                .collect(Collectors.toList());

                List<BigDecimal> values = projections.stream()
                                .map(SalesTemporalProjection::getTotalSales)
                                .collect(Collectors.toList());

                List<Long> orderCounts = projections.stream()
                                .map(SalesTemporalProjection::getTotalOrders)
                                .collect(Collectors.toList());

                SalesChartDto salesChartDto = SalesChartDto.builder()
                                .labels(labels)
                                .values(values)
                                .orderCounts(orderCounts)
                                .build();

                return ApiSuccessDto.of(HttpStatus.OK.value(),
                                messageUtil.getMessage("operation.report.sales.success"),
                                salesChartDto);
        }

        private SalesReportSummaryDto getSalesSummary(SalesReportFiltersDto filters) {
                Pair<Instant, Instant> dates = getDateRange(filters.getStartDate(), filters.getEndDate());
                Map<String, Object> summary = orderRepository.findSalesSummary(
                                dates.getLeft(), dates.getRight(), filters.getOrderStatus());

                BigDecimal totalRevenue = (BigDecimal) summary.get("totalRevenue");
                BigDecimal totalCost = orderRepository.findTotalCostOfGoodsSold(
                                dates.getLeft(), dates.getRight(), filters.getOrderStatus());

                BigDecimal totalMargin = totalRevenue.subtract(totalCost);
                BigDecimal marginPercentage = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                                ? totalMargin.divide(totalRevenue, 4, RoundingMode.HALF_UP)
                                                .multiply(BigDecimal.valueOf(100))
                                : BigDecimal.ZERO;

                return SalesReportSummaryDto.builder()
                                .totalRevenue(totalRevenue)
                                .totalOrders(((Number) summary.get("totalOrders")).longValue())
                                .averageTicket((BigDecimal) summary.get("averageTicket"))
                                .totalDiscount((BigDecimal) summary.get("totalDiscount"))
                                .totalTax((BigDecimal) summary.get("totalTax"))
                                .totalCost(totalCost)
                                .totalMargin(totalMargin)
                                .marginPercentage(marginPercentage)
                                .totalCustomers(((Number) summary.get("totalCustomers")).intValue())
                                .periodStart(filters.getStartDate())
                                .periodEnd(filters.getEndDate())
                                .build();
        }

        private ProductSalesReportDto mapToProductSalesReportDto(ProductSalesProjection projection) {
                return ProductSalesReportDto.builder()
                                .dishId(projection.getDishId())
                                .dishName(projection.getDishName())
                                .categoryName(projection.getCategoryName())
                                .quantitySold(projection.getQuantitySold())
                                .totalRevenue(projection.getTotalRevenue())
                                .totalCost(projection.getTotalCost())
                                .imageUrl(projection.getImageUrl())
                                .build();
        }

        private WaiterPerformanceDto mapWaiterPerformanceProjection(WaiterPerformanceProjection projection,
                        BigDecimal totalRevenue) {
                return WaiterPerformanceDto.builder()
                                .waiterId(projection.getWaiterId())
                                .firstName(projection.getFirstName())
                                .lastName(projection.getLastName())
                                .totalOrders(projection.getTotalOrders())
                                .totalRevenue(projection.getTotalRevenue())
                                .averageTicket(projection.getAverageTicket())
                                .profileImagePath(projection.getProfileImagePath())
                                .build();
        }

        private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {
                return startDate.datesUntil(endDate.plusDays(1))
                                .collect(Collectors.toList());
        }

        private Pair<Instant, Instant> getDateRange(LocalDate startDate, LocalDate endDate) {
                Instant start = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                Instant end = endDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                return Pair.of(start, end);
        }

}