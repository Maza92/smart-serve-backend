package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.report.dashboard.DailySalesDto;
import com.example.demo.dto.report.sales.projections.ProductSalesProjection;
import com.example.demo.dto.report.sales.projections.SalesTemporalProjection;
import com.example.demo.dto.report.sales.projections.WaiterPerformanceProjection;
import com.example.demo.entity.OrderEntity;
import com.example.demo.enums.OrderStatusEnum;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status IN :statuses ORDER BY o.createdAt DESC")
    Page<OrderEntity> findOrdersByDateRangeAndStatuses(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("statuses") List<OrderStatusEnum> statuses, Pageable pageable);

    @EntityGraph(attributePaths = { "orderDetails" })
    @Query("SELECT o FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status IN :statuses ORDER BY o.createdAt DESC")
    List<OrderEntity> findOrdersByDateRangeAndStatuses(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("statuses") List<OrderStatusEnum> statuses);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status IN :statuses")
    BigDecimal sumTotalSalesByDateRangeAndStatuses(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("statuses") List<OrderStatusEnum> statuses);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status IN :statuses")
    long countOrdersByDateRangeAndStatuses(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("statuses") List<OrderStatusEnum> statuses);

    @EntityGraph(attributePaths = { "table" })
    @Query("SELECT o FROM OrderEntity o WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithTable(Integer id);

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status")
    Page<OrderEntity> findByStatus(OrderStatusEnum status, Pageable pageable);

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.status IN :statuses")
    Page<OrderEntity> findByStatues(List<OrderStatusEnum> statuses, Pageable pageable);

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status AND o.id = :id")
    Optional<OrderEntity> findByStatusAndId(OrderStatusEnum status, Integer id);

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.status IN :statuses AND o.id = :id")
    Optional<OrderEntity> findByStatuesAndId(List<OrderStatusEnum> statuses, Integer id);

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.table.id = :tableId")
    Optional<OrderEntity> findByTableId(Integer tableId);

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.table.id = :tableId ORDER BY o.createdAt DESC")
    Optional<OrderEntity> findLastByTableId(Integer tableId);

    @EntityGraph(attributePaths = { "user", "table", "orderDetails", "orderDetails.dish",
            "orderDetails.dish.category" })
    @Query("SELECT o FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.table.id = :tableId ORDER BY o.createdAt DESC LIMIT 1")
    Optional<OrderEntity> findMostRecentByDateRangeAndStatuses(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("tableId") Integer tableId);

    @Query("""
                SELECT new com.example.demo.dto.report.dashboard.DailySalesDto(
                    o.createdAt,
                    SUM(o.totalPrice),
                    COUNT(o.id)
                )
                FROM OrderEntity o
                WHERE o.createdAt BETWEEN :startDate AND :endDate
                GROUP BY o.createdAt
                ORDER BY o.createdAt
            """)
    List<DailySalesDto> findDailySales(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query("""
                SELECT COALESCE(SUM(o.totalPrice), 0)
                FROM OrderEntity o
                WHERE o.createdAt BETWEEN :startDate AND :endDate
            """)
    BigDecimal findTotalRevenueByDateRange(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query("""
                SELECT COUNT(o.id)
                FROM OrderEntity o
                WHERE o.createdAt BETWEEN :startDate AND :endDate
            """)
    Long countOrdersByDateRange(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    @Query(value = """
            SELECT
                d.id as dishId,
                d.name as dishName,
                c.name as categoryName,
                COALESCE(SUM(od.quantity), 0) as quantitySold,
                COALESCE(SUM(od.final_price), 0) as totalRevenue,
                COALESCE(SUM(od.quantity * (
                    SELECT COALESCE(SUM(r.quantity_required * ii.unit_cost), 0)
                    FROM recipe r
                    JOIN inventory_item ii ON r.inventory_item_id = ii.id
                    WHERE r.dish_id = d.id
                )), 0) as totalCost,
                d.image_url as imageUrl
            FROM dish d
            LEFT JOIN category c ON d.category = c.id
            LEFT JOIN order_detail od ON d.id = od.dish_id
            LEFT JOIN orders o ON od.order_id = o.id
            WHERE (CAST(:startDate AS timestamp) IS NULL OR o.created_at >= :startDate)
              AND (CAST(:endDate AS timestamp) IS NULL OR o.created_at <= :endDate)
              AND (CAST(:categoryId AS BIGINT) IS NULL OR c.id = :categoryId)
              AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
              AND d.is_active = true
            GROUP BY d.id, d.name, c.name, d.image_url
            ORDER BY quantitySold DESC
            """, countQuery = """
            SELECT COUNT(*) FROM (
                SELECT d.id
                FROM dish d
                LEFT JOIN category c ON d.category = c.id
                LEFT JOIN order_detail od ON d.id = od.dish_id
                LEFT JOIN orders o ON od.order_id = o.id
                WHERE (CAST(:startDate AS timestamp) IS NULL OR o.created_at >= :startDate)
                  AND (CAST(:endDate AS timestamp) IS NULL OR o.created_at <= :endDate)
                  AND (CAST(:categoryId AS BIGINT) IS NULL OR c.id = :categoryId)
                  AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
                  AND d.is_active = true
                GROUP BY d.id
            ) AS count_table
            """, nativeQuery = true)
    Page<ProductSalesProjection> findProductSalesReport(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("categoryId") Long categoryId,
            @Param("orderStatus") String orderStatus,
            Pageable pageable);

    @Query(value = """
            SELECT
                u.id as waiterId,
                u.first_name as firstName,
                u.last_name as lastName,
                COUNT(o.id) as totalOrders,
                COALESCE(SUM(o.total_price), 0) as totalRevenue,
                COALESCE(AVG(o.total_price), 0) as averageTicket,
                u.profile_image_path as profileImagePath
            FROM "user" u
            LEFT JOIN "role" r ON u.role_id = r.id
            LEFT JOIN orders o ON u.id = o.user_id
                -- CORRECCIÓN: Se añade CAST para ayudar a PostgreSQL a inferir el tipo cuando los parámetros son nulos.
                AND (CAST(:startDate AS timestamp) IS NULL OR o.created_at >= :startDate)
                AND (CAST(:endDate AS timestamp) IS NULL OR o.created_at <= :endDate)
                AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
            WHERE
                r.name IN ('WAITER', 'CASHIER') AND u.active = true
            GROUP BY u.id, u.first_name, u.last_name, u.profile_image_path
            """, countQuery = """
            -- El conteo ahora solo cuenta a los usuarios activos con el rol correcto.
            SELECT COUNT(u.id)
            FROM "user" u
            JOIN "role" r ON u.role_id = r.id
            WHERE r.name IN ('WAITER', 'CASHIER') AND u.active = true
            """, nativeQuery = true)
    Page<WaiterPerformanceProjection> findGeneralWaiterPerformanceReport(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("orderStatus") String orderStatus,
            Pageable pageable);

    @Query(value = """
            SELECT
                CASE EXTRACT(DOW FROM o.created_at)
                    WHEN 0 THEN 'Domingo'
                    WHEN 1 THEN 'Lunes'
                    WHEN 2 THEN 'Martes'
                    WHEN 3 THEN 'Miércoles'
                    WHEN 4 THEN 'Jueves'
                    WHEN 5 THEN 'Viernes'
                    WHEN 6 THEN 'Sábado'
                END as period,
                COALESCE(SUM(o.total_price), 0) as totalSales,
                COUNT(o.id) as totalOrders,
                COALESCE(AVG(o.total_price), 0) as averageTicket,
                EXTRACT(DOW FROM o.created_at) as dayOfWeek,
                null as hourOfDay,
                null as monthOfYear
            FROM orders o
            WHERE (CAST(:startDate AS timestamp) IS NULL OR o.created_at >= :startDate)
            AND (CAST(:endDate AS timestamp) IS NULL OR o.created_at <= :endDate)
            AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
            GROUP BY EXTRACT(DOW FROM o.created_at)
            ORDER BY dayOfWeek
            """, nativeQuery = true)
    List<SalesTemporalProjection> findSalesByDayOfWeek(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("orderStatus") String orderStatus);

    @Query(value = """
            SELECT
                CONCAT(EXTRACT(HOUR FROM o.created_at), ':00') as period,
                COALESCE(SUM(o.total_price), 0) as totalSales,
                COUNT(o.id) as totalOrders,
                COALESCE(AVG(o.total_price), 0) as averageTicket,
                null as dayOfWeek,
                EXTRACT(HOUR FROM o.created_at) as hourOfDay,
                null as monthOfYear
            FROM orders o
            WHERE (CAST(:startDate AS timestamp) IS NULL OR o.created_at >= :startDate)
            AND (CAST(:endDate AS timestamp) IS NULL OR o.created_at <= :endDate)
            AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
            GROUP BY EXTRACT(HOUR FROM o.created_at)
            ORDER BY hourOfDay
            """, nativeQuery = true)
    List<SalesTemporalProjection> findSalesByHour(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("orderStatus") String orderStatus);

    @Query(value = """
            SELECT
                TO_CHAR(o.created_at, 'MM/YYYY') as period,
                COALESCE(SUM(o.total_price), 0) as totalSales,
                COUNT(o.id) as totalOrders,
                COALESCE(AVG(o.total_price), 0) as averageTicket,
                null as dayOfWeek,
                null as hourOfDay,
                EXTRACT(MONTH FROM o.created_at) as monthOfYear
            FROM orders o
            WHERE (CAST(:startDate AS date) IS NULL OR DATE(o.created_at) >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR DATE(o.created_at) <= :endDate)
            AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
            GROUP BY TO_CHAR(o.created_at, 'MM/YYYY'), EXTRACT(YEAR FROM o.created_at), EXTRACT(MONTH FROM o.created_at)
            ORDER BY EXTRACT(YEAR FROM o.created_at), monthOfYear
            """, nativeQuery = true)
    List<SalesTemporalProjection> findSalesByMonth(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("orderStatus") String orderStatus);

    @Query(value = """
            SELECT
                TO_CHAR(o.created_at, 'DD/MM') as period,
                COALESCE(SUM(o.total_price), 0) as totalSales,
                COUNT(o.id) as totalOrders,
                COALESCE(AVG(o.total_price), 0) as averageTicket,
                null as dayOfWeek,
                null as hourOfDay,
                null as monthOfYear
            FROM orders o
            WHERE (CAST(:startDate AS date) IS NULL OR DATE(o.created_at) >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR DATE(o.created_at) <= :endDate)
            AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
            GROUP BY DATE(o.created_at)
            ORDER BY DATE(o.created_at)
            """, nativeQuery = true)
    List<SalesTemporalProjection> findSalesByDay(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("orderStatus") String orderStatus);

    @Query(value = """
            SELECT
                COALESCE(SUM(o.total_price), 0) as totalRevenue,
                COUNT(o.id) as totalOrders,
                COALESCE(AVG(o.total_price), 0) as averageTicket,
                COALESCE(SUM(o.discount_amount), 0) as totalDiscount,
                COALESCE(SUM(o.tax_amount), 0) as totalTax,
                COALESCE(SUM(o.guests_count), 0) as totalCustomers
            FROM orders o
            WHERE (CAST(:startDate AS date) IS NULL OR DATE(o.created_at) >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR DATE(o.created_at) <= :endDate)
            AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
            """, nativeQuery = true)
    Map<String, Object> findSalesSummary(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("orderStatus") String orderStatus);

    @Query(value = """
            SELECT
                u.id as waiterId,
                u.first_name as firstName,
                u.last_name as lastName,
                COUNT(o.id) as totalOrders,
                COALESCE(SUM(o.total_price), 0) as totalRevenue,
                COALESCE(AVG(o.total_price), 0) as averageTicket,
                u.profile_image_path as profileImagePath
            FROM "user" u
            LEFT JOIN orders o ON u.id = o.user_id
            LEFT JOIN "role" r ON u.role_id = r.id
            WHERE r.name IN ('WAITER', 'CASHIER')
            AND (CAST(:startDate AS date) IS NULL OR DATE(o.created_at) >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR DATE(o.created_at) <= :endDate)
            AND u.active = true
            GROUP BY u.id, u.first_name, u.last_name, u.profile_image_path
            ORDER BY totalRevenue DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<WaiterPerformanceProjection> findTopWaiters(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("limit") Integer limit);

    @Query(value = """
            SELECT
                COALESCE(SUM(od.quantity * (
                    SELECT COALESCE(SUM(r.quantity_required * ii.unit_cost), 0)
                    FROM recipe r
                    JOIN inventory_item ii ON r.inventory_item_id = ii.id
                    WHERE r.dish_id = od.dish_id
                )), 0) as totalCost
            FROM order_detail od
            JOIN orders o ON od.order_id = o.id
            WHERE (CAST(:startDate AS date) IS NULL OR DATE(o.created_at) >= :startDate)
            AND (CAST(:endDate AS date) IS NULL OR DATE(o.created_at) <= :endDate)
            AND (CAST(:orderStatus AS TEXT) IS NULL OR o.status = :orderStatus)
            """, nativeQuery = true)
    BigDecimal findTotalCostOfGoodsSold(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("orderStatus") String orderStatus);
}