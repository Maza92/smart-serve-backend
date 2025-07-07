package com.example.demo.service.data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.inventory.dashboard.CategoriesMetricDto;
import com.example.demo.dto.inventory.dashboard.InventoryMetricsDto;
import com.example.demo.dto.inventory.dashboard.LowStockItemDto;
import com.example.demo.dto.inventory.dashboard.LowStockMetricDto;
import com.example.demo.dto.inventory.dashboard.TotalItemsMetricDto;
import com.example.demo.dto.inventory.dashboard.TotalValueMetricDto;
import com.example.demo.enums.TrendTypeEnum;
import com.example.demo.repository.InventoryDashboardRepository;
import com.example.demo.utils.MessageUtils;

import java.time.temporal.ChronoUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryMetricsCalculatorService {
    private final InventoryDashboardRepository dashboardRepository;
    private final MessageUtils messageUtils;

    public InventoryMetricsDto calculateMetrics() {
        Instant now = Instant.now();
        Instant lastMonth = now.minus(30, ChronoUnit.DAYS);

        return InventoryMetricsDto.builder()
                .totalItems(calculateTotalItems(now, lastMonth))
                .lowStock(calculateLowStock(now, lastMonth))
                .totalValue(calculateTotalValue(now, lastMonth))
                .categories(calculateCategories(now, lastMonth))
                .build();
    }

    private TotalItemsMetricDto calculateTotalItems(Instant now, Instant lastMonth) {
        Integer currentCount = dashboardRepository.countActiveItems();
        log.info("Current count: {}", currentCount);
        Integer lastMonthCount = dashboardRepository.countActiveItemsAtDate(lastMonth);
        log.info("Last month count: {}", lastMonthCount);
        Double percentageChange = calculatePercentageChange(currentCount, lastMonthCount);
        log.info("Percentage change: {}", percentageChange);
        TrendTypeEnum trend = determineTrend(percentageChange);
        log.info("Trend: {}", trend);

        return TotalItemsMetricDto.builder()
                .currentCount(currentCount)
                .percentageChange(Math.abs(percentageChange))
                .trend(trend)
                .changeDescription(formatChangeDescription(percentageChange, "item.total.metrics"))
                .build();
    }

    private LowStockMetricDto calculateLowStock(Instant now, Instant lastMonth) {
        List<LowStockItemDto> lowStockItems = dashboardRepository.getLowStockItems();
        Integer currentCount = lowStockItems.size();

        Integer lastMonthCount = dashboardRepository.countLowStockItemsAtDate(lastMonth);
        Double percentageChange = calculatePercentageChange(currentCount, lastMonthCount);
        TrendTypeEnum trend = determineTrend(percentageChange);

        return LowStockMetricDto.builder()
                .currentCount(currentCount)
                .percentageChange(Math.abs(percentageChange))
                .trend(trend)
                .changeDescription(formatChangeDescription(percentageChange, "item.total.metrics"))
                .items(lowStockItems)
                .build();
    }

    private TotalValueMetricDto calculateTotalValue(Instant now, Instant lastMonth) {
        BigDecimal currentValue = dashboardRepository.calculateTotalInventoryValue();
        BigDecimal lastMonthValue = dashboardRepository.calculateTotalInventoryValueAtDate(lastMonth);

        Double percentageChange = calculatePercentageChange(
                currentValue.doubleValue(),
                lastMonthValue.doubleValue());
        TrendTypeEnum trend = determineTrend(percentageChange);

        return TotalValueMetricDto.builder()
                .currentValue(currentValue)
                .percentageChange(Math.abs(percentageChange))
                .trend(trend)
                .changeDescription(formatChangeDescription(percentageChange, "item.total.metrics"))
                .build();
    }

    private CategoriesMetricDto calculateCategories(Instant now, Instant lastMonth) {
        Integer currentCount = dashboardRepository.countActiveCategories();
        Integer lastMonthCount = dashboardRepository.countActiveCategoriesAtDate(lastMonth);
        Integer newCategories = currentCount - lastMonthCount;

        return CategoriesMetricDto.builder()
                .currentCount(currentCount)
                .newCategoriesThisMonth(Math.max(0, newCategories))
                .changeDescription(newCategories > 0
                        ? "+" + newCategories + " " + messageUtils.getMessage("category.total.metrics.new")
                        : messageUtils.getMessage("category.total.metrics.low"))
                .build();
    }

    private Double calculatePercentageChange(Number current, Number previous) {
        if (previous.doubleValue() == 0)
            return 0.0;
        return ((current.doubleValue() - previous.doubleValue()) / previous.doubleValue()) * 100;
    }

    private TrendTypeEnum determineTrend(Double percentageChange) {
        if (percentageChange > 1)
            return TrendTypeEnum.UP;
        if (percentageChange < -1)
            return TrendTypeEnum.DOWN;
        return TrendTypeEnum.STABLE;
    }

    private String formatChangeDescription(Double percentageChange, String suffix) {
        String sign = percentageChange >= 0 ? "+" : "";
        return String.format("%s%.1f%% %s", sign, percentageChange, messageUtils.getMessage(suffix));
    }
}
