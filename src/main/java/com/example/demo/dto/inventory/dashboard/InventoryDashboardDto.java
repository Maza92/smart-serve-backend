package com.example.demo.dto.inventory.dashboard;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDashboardDto {
    private InventoryMetricsDto metrics;
    private List<CategoryStockLevelDto> categoryStockLevels;
    private List<RecentActivityDto> recentActivities;
    private Instant lastUpdated;
}
