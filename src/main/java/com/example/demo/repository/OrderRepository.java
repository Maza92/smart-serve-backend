package com.example.demo.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}