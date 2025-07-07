package com.example.demo.service.inventory.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.data.BatchSaveResultDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;
import com.example.demo.dto.inventory.UpdateInventoryItemsStockBatchDto;
import com.example.demo.dto.inventory.dashboard.InventoryMetricsDto;
import com.example.demo.dto.inventory.dashboard.RecentActivityDto;
import com.example.demo.dto.orderDetail.CreateOrderDetailDto;
import com.example.demo.dto.orderDetail.ModificationDto;
import com.example.demo.entity.DishEntity;
import com.example.demo.entity.InventoryItemEntity;
import com.example.demo.entity.InventoryMovementEntity;
import com.example.demo.entity.OrderDetailEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.ModificationTypeEnum;
import com.example.demo.enums.MovementReasonEnum;
import com.example.demo.enums.MovementTypeEnum;
import com.example.demo.enums.ReferenceTypeEnum;
import com.example.demo.enums.UpdateTypeEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.InventoryItemRepository;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.service.data.BatchSaveService;
import com.example.demo.service.data.InventoryMetricsCalculatorService;
import com.example.demo.service.inventory.IInventoryService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.service.unitConversion.IUnitConversionService;
import com.example.demo.utils.InventoryChangedEvent;
import com.example.demo.utils.MessageUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryMovementRepository movementRepository;
    private final RecipeRepository recipeRepository;
    private final ApiExceptionFactory apiExceptionFactory;
    private final ISecurityContextService securityContextService;
    private final IUnitConversionService unitConversionService;
    private final BatchSaveService batchSaveService;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivityServiceImpl activityService;
    private final InventoryMetricsCalculatorService metricsCalculator;
    private final MessageUtils messageUtils;

    @Override
    @Transactional
    public ApiSuccessDto<Void> updateStock(UpdateInventoryItemStockDto request) {
        InventoryItemEntity item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.inventory.update.not.found"));

        BigDecimal quantityBefore = item.getStockQuantity();
        BigDecimal quantityAfter = switch (request.getMovementType()) {
            case IN, ADJUSTMENT_IN, TRANSFER_IN -> quantityBefore.add(request.getQuantityChanged());
            case OUT, ADJUSTMENT_OUT, TRANSFER_OUT -> quantityBefore.subtract(request.getQuantityChanged());
            default ->
                throw apiExceptionFactory.badRequestException("operation.inventory.update.invalid.movement.type");
        };

        InventoryMovementEntity movement = InventoryMovementEntity.builder()
                .inventoryItem(item)
                .movementType(request.getMovementType())
                .quantityBefore(quantityBefore)
                .quantityAfter(quantityAfter)
                .quantityChanged(request.getQuantityChanged())
                .unitCostAtTime(request.getUnitCostAtTime())
                .reason(request.getReason())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .user(securityContextService.getUser())
                .build();

        InventoryMovementEntity savedMovement = movementRepository.save(movement);

        item.setStockQuantity(quantityAfter);
        itemRepository.save(item);

        RecentActivityDto activity = activityService.mapInventoryMovementToActivity(savedMovement);
        eventPublisher.publishEvent(new InventoryChangedEvent(this, UpdateTypeEnum.NEW_ACTIVITY, activity));

        if (savedMovement.getInventoryItem().getMinStockLevel().compareTo(quantityAfter) > 0) {
            InventoryMetricsDto metrics = metricsCalculator.calculateMetrics();
            eventPublisher.publishEvent(new InventoryChangedEvent(this, UpdateTypeEnum.METRICS_UPDATE, metrics));
        }

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.inventory.update.success"));
    }

    @Override
    @Transactional
    public ApiSuccessDto<Void> updateStocksBatch(UpdateInventoryItemsStockBatchDto requests) {
        List<UpdateInventoryItemStockDto> items = requests.getItems();
        List<Integer> itemIds = items.stream().map(UpdateInventoryItemStockDto::getItemId).distinct().toList();
        List<InventoryItemEntity> itemsFromDb = itemRepository.findAllById(itemIds);

        Map<Integer, InventoryItemEntity> itemMap = itemsFromDb.stream()
                .collect(Collectors.toMap(InventoryItemEntity::getId, item -> item));
        if (itemMap.size() != itemIds.size()) {
            throw apiExceptionFactory.entityNotFound("operation.inventory.update.batch.not.found");
        }

        List<InventoryItemEntity> itemsToUpdate = new ArrayList<>(items.size());
        List<InventoryMovementEntity> movementsToCreate = new ArrayList<>(items.size());
        UserEntity currentUser = securityContextService.getUser();

        for (UpdateInventoryItemStockDto request : items) {
            InventoryItemEntity item = itemMap.get(request.getItemId());

            BigDecimal quantityBefore = item.getStockQuantity();
            BigDecimal quantityAfter = switch (request.getMovementType()) {
                case IN, ADJUSTMENT_IN, TRANSFER_IN -> quantityBefore.add(request.getQuantityChanged());
                case OUT, ADJUSTMENT_OUT, TRANSFER_OUT -> quantityBefore.subtract(request.getQuantityChanged());
                default ->
                    throw apiExceptionFactory.badRequestException("operation.inventory.update.invalid.movement.type");
            };

            InventoryMovementEntity movement = InventoryMovementEntity.builder()
                    .inventoryItem(item)
                    .movementType(request.getMovementType())
                    .quantityBefore(quantityBefore)
                    .quantityAfter(quantityAfter)
                    .quantityChanged(request.getQuantityChanged())
                    .unitCostAtTime(request.getUnitCostAtTime())
                    .reason(request.getReason())
                    .referenceType(request.getReferenceType())
                    .referenceId(request.getReferenceId())
                    .notes(request.getNotes())
                    .user(currentUser)
                    .build();

            movementsToCreate.add(movement);

            item.setStockQuantity(quantityAfter);
            itemsToUpdate.add(item);
        }

        BatchSaveResultDto movementResult = batchSaveService.saveBatch(movementsToCreate, movementRepository);
        BatchSaveResultDto itemResult = batchSaveService.saveBatch(itemsToUpdate, itemRepository);

        if (movementResult.getErrorCount() > 0 || itemResult.getErrorCount() > 0)
            throw apiExceptionFactory.badRequestException("operation.inventory.update.batch.failed");

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.inventory.update.batch.success"));
    }

    @Override
    @Transactional
    public CompletableFuture<ApiSuccessDto<Void>> updateStocksBatchAsync(UpdateInventoryItemsStockBatchDto requests) {
        return CompletableFuture.completedFuture(updateStocksBatch(requests));
    }

    @Override
    public Boolean isStockAvailable(int itemId, int quantity) {
        InventoryItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.inventory.item.get.by.id.not.found"));

        BigDecimal itemStockQuantity = item.getStockQuantity();
        BigDecimal availableStockQuantity = itemStockQuantity.subtract(BigDecimal.valueOf(quantity));
        return availableStockQuantity.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public void checkStockForOrder(List<CreateOrderDetailDto> details, Map<Integer, DishEntity> dishes) {
        Map<Integer, BigDecimal> requiredIngredientsMap = new HashMap<>();

        for (CreateOrderDetailDto detail : details) {
            DishEntity dish = dishes.get(detail.getDishId());
            int quantity = detail.getQuantity();

            List<RecipeEntity> dishRecipe = recipeRepository.findAllByDishWithIngredients(dish.getId());

            if (dishRecipe.isEmpty())
                throw apiExceptionFactory.badRequestException("operation.dish.noContainsRecipe");

            for (RecipeEntity recipeIngredient : dishRecipe) {
                Integer ingredientId = recipeIngredient.getInventoryItem().getId();
                BigDecimal requiredPerDish = recipeIngredient.getQuantityRequired();
                BigDecimal totalRequired = requiredPerDish.multiply(BigDecimal.valueOf(quantity));

                requiredIngredientsMap.merge(ingredientId, totalRequired, BigDecimal::add);
            }
        }

        if (requiredIngredientsMap.isEmpty())
            throw apiExceptionFactory.badRequestException("operation.dish.noContainsRequiredIngredients");

        Set<Integer> allIngredientsIds = requiredIngredientsMap.keySet();
        Map<Integer, InventoryItemEntity> currentStockMap = itemRepository.findAllById(allIngredientsIds)
                .stream()
                .collect(Collectors.toMap(InventoryItemEntity::getId, item -> item));

        for (Map.Entry<Integer, BigDecimal> entry : requiredIngredientsMap.entrySet()) {
            Integer ingredientId = entry.getKey();
            BigDecimal quantityNeeded = entry.getValue();

            InventoryItemEntity stockItem = currentStockMap.get(ingredientId);

            if (stockItem == null || !stockItem.getIsActive()) {
                throw apiExceptionFactory.badRequestException("operation.inventory.item.not.available", ingredientId);
            }

            if (stockItem.getStockQuantity().compareTo(quantityNeeded) < 0) {
                throw apiExceptionFactory.badRequestException(
                        "operation.inventory.insufficient.stock",
                        stockItem.getName(),
                        stockItem.getStockQuantity(),
                        quantityNeeded);
            }
        }
    }

    @Override
    public void updateStockForOrder(OrderEntity order) {
        List<InventoryMovementEntity> movements = new ArrayList<>();

        for (OrderDetailEntity detail : order.getOrderDetails()) {
            DishEntity dish = detail.getDish();
            int quantityOrdered = detail.getQuantity();

            List<RecipeEntity> dishRecipe = recipeRepository.findAllByDishWithIngredients(dish.getId());

            for (RecipeEntity recipeIngredient : dishRecipe) {
                BigDecimal quantityToDecrease = calculateQuantityToDecrease(recipeIngredient, quantityOrdered);

                createMovementAndUpdateStock(recipeIngredient.getInventoryItem(),
                        quantityToDecrease, movements, order);
            }

            if (detail.getModifications() != null) {
                processModifications(detail.getModifications(), quantityOrdered, movements, order);
            }
        }

        movementRepository.saveAll(movements);
    }

    private BigDecimal calculateQuantityToDecrease(RecipeEntity recipeIngredient, int quantityOrdered) {
        InventoryItemEntity item = recipeIngredient.getInventoryItem();

        BigDecimal convertedQuantity = unitConversionService.convertUnit(
                recipeIngredient.getQuantityRequired(),
                recipeIngredient.getUnit().getId(),
                item.getUnit().getId());

        return convertedQuantity.multiply(BigDecimal.valueOf(quantityOrdered));
    }

    private void createMovementAndUpdateStock(InventoryItemEntity item, BigDecimal quantityChange,
            List<InventoryMovementEntity> movements, OrderEntity order) {
        BigDecimal stockBefore = item.getStockQuantity();
        BigDecimal stockAfter = stockBefore.subtract(quantityChange);

        item.setStockQuantity(stockAfter);
        item.setLastUpdated(Instant.now());

        InventoryMovementEntity movement = new InventoryMovementEntity();
        movement.setInventoryItem(item);
        movement.setMovementType(
                quantityChange.compareTo(BigDecimal.ZERO) > 0 ? MovementTypeEnum.OUT : MovementTypeEnum.IN);
        movement.setReason(MovementReasonEnum.RECIPE_USAGE);
        movement.setQuantityChanged(quantityChange.abs());
        movement.setQuantityBefore(stockBefore);
        movement.setQuantityAfter(stockAfter);
        movement.setUnitCostAtTime(item.getUnitCost());
        movement.setMovementDate(Instant.now());
        movement.setUser(order.getUser());
        movement.setReferenceId(order.getId());
        movement.setReferenceType(ReferenceTypeEnum.ORDER);

        movements.add(movement);
    }

    private void processModifications(JsonNode modifications, int quantityOrdered,
            List<InventoryMovementEntity> movements, OrderEntity order) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ModificationDto[] modificationArray = mapper.treeToValue(modifications, ModificationDto[].class);

            for (ModificationDto modification : modificationArray) {
                if (modification.getAction() == ModificationTypeEnum.NOTE)
                    continue;

                InventoryItemEntity item = itemRepository.findById(modification.getInventoryItemId())
                        .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.inventory.item.not.found"));

                BigDecimal convertedQuantity = unitConversionService.convertUnit(
                        modification.getQuantityChange(),
                        modification.getUnitId(),
                        item.getUnit().getId());

                BigDecimal totalQuantityChange = convertedQuantity.multiply(BigDecimal.valueOf(quantityOrdered));

                switch (modification.getAction()) {
                    case ADD -> {
                        createMovementAndUpdateStock(item, totalQuantityChange, movements, order);
                    }
                    case REMOVE -> {
                        createMovementAndUpdateStock(item, totalQuantityChange.negate(), movements, order);
                    }
                    case EXTRA -> {
                        createMovementAndUpdateStock(item, totalQuantityChange, movements, order);
                    }
                    case LESS -> {
                        createMovementAndUpdateStock(item, totalQuantityChange.negate(), movements, order);
                    }
                    case NOTE -> {
                        // Para nota, no hacemos nada. Orientado a acciones de preparación
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error procesando modificaciones: {}", modifications.toString(), e);
            throw apiExceptionFactory.badRequestException("operation.inventory.update.invalid.modifications");
        }
    }

}
