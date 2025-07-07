package com.example.demo.service.inventory.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.inventory.dashboard.RecentActivityDto;
import com.example.demo.entity.InventoryMovementEntity;
import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.TransactionEntity;
import com.example.demo.enums.ActivityTypeEnum;
import com.example.demo.enums.MovementReasonEnum;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.NotificationsRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl {
    private final InventoryMovementRepository inventoryMovementRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationsRepository notificationRepository;
    private final MessageUtils messageUtils;

    Integer ACTIVITIES_LIMIT = 10;

    public List<RecentActivityDto> getRecentActivities() {
        List<RecentActivityDto> activities = new ArrayList<>();

        activities.addAll(getInventoryActivities(ACTIVITIES_LIMIT / 3));
        activities.addAll(getTransactionActivities(ACTIVITIES_LIMIT / 3));
        activities.addAll(getSystemActivities(ACTIVITIES_LIMIT / 3));

        return activities.stream()
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .limit(ACTIVITIES_LIMIT)
                .map(this::enrichWithTimeAgo)
                .collect(Collectors.toList());
    }

    private List<RecentActivityDto> getInventoryActivities(int limit) {
        return inventoryMovementRepository.findRecentMovements(PageRequest.of(0, limit))
                .stream()
                .map(this::mapInventoryMovementToActivity)
                .collect(Collectors.toList());
    }

    private List<RecentActivityDto> getTransactionActivities(int limit) {
        return transactionRepository.findRecentTransactions(PageRequest.of(0, limit))
                .stream()
                .map(this::mapTransactionToActivity)
                .collect(Collectors.toList());
    }

    private RecentActivityDto mapTransactionToActivity(TransactionEntity transaction) {
        return RecentActivityDto.builder()
                .id("trans_" + transaction.getId())
                .activityType(ActivityTypeEnum.ORDER_PAY)
                .description(messageUtils.getMessage("transaction.description", transaction.getPaymentMethod(),
                        transaction.getAmount()))
                .userName(transaction.getUser().getCompleteNames())
                .timestamp(transaction.getTransactionDate())
                .metadata(Map.of(
                        "amount", transaction.getAmount(),
                        "type", transaction.getTransactionType()))
                .build();
    }

    private List<RecentActivityDto> getSystemActivities(int limit) {
        return notificationRepository.findRecentNotifications(PageRequest.of(0, limit))
                .stream()
                .map(this::mapNotificationToActivity)
                .collect(Collectors.toList());
    }

    private RecentActivityDto mapNotificationToActivity(NotificationsEntity notification) {
        return RecentActivityDto.builder()
                .id("notif_" + notification.getId())
                .activityType(ActivityTypeEnum.LOW_STOCK_ALERT)
                .description(notification.getMessage())
                .userName(notification.getUser().getCompleteNames())
                .timestamp(notification.getCreatedAt())
                .metadata(Map.of(
                        "itemName", notification.getMessage()))
                .build();
    }

    public RecentActivityDto mapInventoryMovementToActivity(InventoryMovementEntity movement) {
        return RecentActivityDto.builder()
                .id("inv_" + movement.getId())
                .activityType(mapMovementTypeToActivityType(movement.getReason()))
                .description(buildInventoryDescription(movement))
                .userName(movement.getUser().getCompleteNames())
                .timestamp(movement.getMovementDate())
                .metadata(Map.of(
                        "itemName", movement.getInventoryItem().getName(),
                        "quantity", movement.getQuantityChanged(),
                        "reason", movement.getReason()))
                .build();
    }

    private ActivityTypeEnum mapMovementTypeToActivityType(MovementReasonEnum reason) {
        return switch (reason) {
            case PURCHASE -> ActivityTypeEnum.INVENTORY_PURCHASE;
            case RECIPE_USAGE -> ActivityTypeEnum.RECIPE_USAGE;
            case MANUAL_ADJUSTMENT -> ActivityTypeEnum.INVENTORY_ADJUSTMENT;
            case EXPIRY -> ActivityTypeEnum.INVENTORY_EXPIRY;
            case DAMAGE -> ActivityTypeEnum.INVENTORY_DAMAGE;
            case TRANSFER -> ActivityTypeEnum.INVENTORY_TRANSFER;
        };
    }

    private String buildInventoryDescription(InventoryMovementEntity movement) {
        return switch (movement.getReason()) {
            case PURCHASE ->
                messageUtils.getMessage("inventory.reason.purchase", movement.getInventoryItem().getName());
            case RECIPE_USAGE ->
                messageUtils.getMessage("inventory.reason.recipeUsage", movement.getInventoryItem().getName());
            case MANUAL_ADJUSTMENT ->
                messageUtils.getMessage("inventory.reason.manualAdjustment", movement.getInventoryItem().getName());
            case EXPIRY -> messageUtils.getMessage("inventory.reason.expiry", movement.getInventoryItem().getName());
            case DAMAGE -> messageUtils.getMessage("inventory.reason.damage", movement.getInventoryItem().getName());
            case TRANSFER ->
                messageUtils.getMessage("inventory.reason.transfer", movement.getInventoryItem().getName());
        };
    }

    private RecentActivityDto enrichWithTimeAgo(RecentActivityDto activity) {
        activity.setTimeAgo(calculateTimeAgo(activity.getTimestamp()));
        return activity;
    }

    private String calculateTimeAgo(Instant timestamp) {
        Duration duration = Duration.between(timestamp, Instant.now());

        if (duration.toMinutes() < 60) {
            return messageUtils.getMessage("time.ago.minute", duration.toMinutes());
        } else if (duration.toHours() < 24) {
            return messageUtils.getMessage("time.ago.hour", duration.toHours());
        } else {
            return messageUtils.getMessage("time.ago.day", duration.toDays());
        }
    }

}
