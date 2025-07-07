package com.example.demo.service.inventory.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.dashboard.CategoryStockLevelDto;
import com.example.demo.dto.inventory.dashboard.DashboardUpdateDto;
import com.example.demo.dto.inventory.dashboard.InventoryDashboardDto;
import com.example.demo.repository.InventoryDashboardRepository;
import com.example.demo.service.data.CacheService;
import com.example.demo.service.data.InventoryMetricsCalculatorService;
import com.example.demo.service.inventory.IInventoryDashboardService;
import com.example.demo.service.webSocket.IStompMessagingService;
import com.example.demo.utils.InventoryChangedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryDashboardServiceImpl implements IInventoryDashboardService {
    private final InventoryDashboardRepository dashboardRepository;
    private final InventoryMetricsCalculatorService metricsCalculator;
    private final ActivityServiceImpl activityService;
    private final IStompMessagingService messagingTemplate;
    private final CacheManager cacheManager;
    private final CacheService cacheService;

    @Cacheable(value = "dashboardData", key = "'inventory-dashboard'")
    @Transactional(readOnly = true)
    public ApiSuccessDto<InventoryDashboardDto> getDashboardData() {
        InventoryDashboardDto dashboardData = InventoryDashboardDto.builder()
                .metrics(metricsCalculator.calculateMetrics())
                .categoryStockLevels(calculateCategoryStockLevels())
                .recentActivities(activityService.getRecentActivities())
                .lastUpdated(Instant.now())
                .build();

        return ApiSuccessDto.of(HttpStatus.OK.value(), "operation.inventory.dashboard.success", dashboardData);
    }

    private List<CategoryStockLevelDto> calculateCategoryStockLevels() {
        return dashboardRepository.getCategoryStockLevels();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInventoryChangedEvent(InventoryChangedEvent event) {
        cacheService.clearInventoryDashboardCache();

        DashboardUpdateDto update = DashboardUpdateDto.builder()
                .updateType(event.getUpdateType())
                .data(event.getData())
                .timestamp(Instant.now())
                .build();

        broadcastUpdate(update);
    }

    @Async
    public void broadcastUpdate(DashboardUpdateDto update) {
        messagingTemplate.sendMessage("/topic/dashboard/inventory", update);
    }
}
