package com.example.demo.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.demo.dto.inventory.dashboard.TrendAnalysisDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final EntityManager entityManager;

    public List<TrendAnalysisDto> getInventoryTrends(int months) {
        String sql = """
                WITH monthly_stats AS (
                    SELECT
                        DATE_TRUNC('month', created_at) as month,
                        COUNT(*) as total_items,
                        SUM(stock_quantity * unit_cost) as total_value,
                        COUNT(CASE WHEN stock_quantity <= min_stock_level THEN 1 END) as low_stock_count
                    FROM inventory_item
                    WHERE is_active = true
                    AND created_at >= CURRENT_DATE - INTERVAL '%d months'
                    GROUP BY DATE_TRUNC('month', created_at)
                ),
                movement_stats AS (
                    SELECT
                        DATE_TRUNC('month', movement_date) as month,
                        reason,
                        COUNT(*) as movement_count,
                        SUM(ABS(quantity_changed)) as total_quantity_moved
                    FROM inventory_movement
                    WHERE movement_date >= CURRENT_DATE - INTERVAL '%d months'
                    GROUP BY DATE_TRUNC('month', movement_date), reason
                )
                SELECT
                    ms.month,
                    ms.total_items,
                    ms.total_value,
                    ms.low_stock_count,
                    COALESCE(mvs.movement_count, 0) as total_movements,
                    COALESCE(mvs.total_quantity_moved, 0) as total_quantity_moved
                FROM monthly_stats ms
                LEFT JOIN (
                    SELECT month,
                           SUM(movement_count) as movement_count,
                           SUM(total_quantity_moved) as total_quantity_moved
                    FROM movement_stats
                    GROUP BY month
                ) mvs ON ms.month = mvs.month
                ORDER BY ms.month DESC
                """.formatted(months, months);

        Query query = entityManager.createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(this::mapToTrendAnalysisDTO)
                .collect(Collectors.toList());
    }

    private TrendAnalysisDto mapToTrendAnalysisDTO(Object[] row) {
        return TrendAnalysisDto.builder()
                .month((Date) row[0])
                .totalItems(((Number) row[1]).intValue())
                .totalValue((BigDecimal) row[2])
                .lowStockCount(((Number) row[3]).intValue())
                .totalMovements(((Number) row[4]).intValue())
                .totalQuantityMoved((BigDecimal) row[5])
                .build();
    }

}
