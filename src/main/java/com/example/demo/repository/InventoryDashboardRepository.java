package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.inventory.dashboard.CategoryStockLevelDto;
import com.example.demo.dto.inventory.dashboard.LowStockItemDto;
import com.example.demo.entity.InventoryItemEntity;

public interface InventoryDashboardRepository extends JpaRepository<InventoryItemEntity, Integer> {
    @Query("SELECT COUNT(i) FROM InventoryItemEntity i WHERE i.isActive = true")
    Integer countActiveItems();

    @Query("""
            SELECT COUNT(i) FROM InventoryItemEntity i
            WHERE i.isActive = true
            AND i.createdAt <= :date
            """)
    Integer countActiveItemsAtDate(@Param("date") Instant date);

    @Query("""
            SELECT new com.example.demo.dto.inventory.dashboard.LowStockItemDto(
                i.id,
                i.name,
                i.stockQuantity,
                i.minStockLevel,
                u.name,
                c.name
            )
            FROM InventoryItemEntity i
            LEFT JOIN i.unit u
            LEFT JOIN i.category c
            WHERE i.isActive = true
            AND i.stockQuantity <= i.minStockLevel
            ORDER BY (i.stockQuantity / i.minStockLevel) ASC
            """)
    List<LowStockItemDto> getLowStockItems();

    @Query("""
            SELECT COUNT(i) FROM InventoryItemEntity i
            WHERE i.isActive = true
            AND i.stockQuantity <= i.minStockLevel
            AND i.createdAt <= :date
            """)
    Integer countLowStockItemsAtDate(@Param("date") Instant date);

    @Query("""
            SELECT COALESCE(SUM(i.stockQuantity * i.unitCost), 0)
            FROM InventoryItemEntity i
            WHERE i.isActive = true
            """)
    BigDecimal calculateTotalInventoryValue();

    @Query(value = """
                WITH movements_after_date AS (
                    SELECT
                        im.inventory_item_id,
                        SUM(im.quantity_changed) AS total_changed_after_date
                    FROM
                        inventory_movement im
                    WHERE
                        im.movement_date > :date
                    GROUP BY
                        im.inventory_item_id
                )
                SELECT
                    COALESCE(SUM(
                        (i.stock_quantity - COALESCE(mad.total_changed_after_date, 0)) * i.unit_cost
                    ), 0)
                FROM
                    inventory_item i
                LEFT JOIN
                    movements_after_date mad ON i.id = mad.inventory_item_id
                WHERE
                    i.is_active = true
                    AND i.created_at <= :date
            """, nativeQuery = true)
    BigDecimal calculateTotalInventoryValueAtDate(@Param("date") Instant date);

    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.isActive = true")
    Integer countActiveCategories();

    @Query("""
            SELECT COUNT(c) FROM CategoryEntity c
            WHERE c.isActive = true
            AND c.createdAt <= :date
            """)
    Integer countActiveCategoriesAtDate(@Param("date") Instant date);

    @Query("""
                  SELECT new com.example.demo.dto.inventory.dashboard.CategoryStockLevelDto(
                      c.name,
                      CASE
                          WHEN COUNT(i.id) > 0 THEN
                              (SUM(CASE WHEN i.stockQuantity > i.minStockLevel THEN 1 ELSE 0 END) * 100.0 / COUNT(i.id))
                          ELSE
                              100.0
                      END,
                      COUNT(i.id),
                      SUM(CASE WHEN i.stockQuantity <= i.minStockLevel THEN 1 ELSE 0 END),
                      COALESCE(SUM(i.stockQuantity * i.unitCost), 0.0)
                  )
                  FROM CategoryEntity c
                  LEFT JOIN InventoryItemEntity i ON c.id = i.category.id AND i.isActive = true
                  WHERE c.isActive = true
                  GROUP BY c.id, c.name
                  ORDER BY c.name
            """)
    List<CategoryStockLevelDto> getCategoryStockLevels();
}
