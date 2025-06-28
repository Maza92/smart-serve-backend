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

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.data.BatchSaveResultDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;
import com.example.demo.dto.inventory.UpdateInventoryItemsStockBatchDto;
import com.example.demo.dto.orderDetail.CreateOrderDetailDto;
import com.example.demo.entity.DishEntity;
import com.example.demo.entity.InventoryItemEntity;
import com.example.demo.entity.InventoryMovementEntity;
import com.example.demo.entity.OrderDetailEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.MovementReasonEnum;
import com.example.demo.enums.MovementTypeEnum;
import com.example.demo.enums.ReferenceTypeEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.InventoryItemRepository;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.service.data.BatchSaveService;
import com.example.demo.service.inventory.IInventoryService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.utils.MessageUtils;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryMovementRepository movementRepository;
    private final RecipeRepository recipeRepository;
    private final ApiExceptionFactory apiExceptionFactory;
    private final ISecurityContextService securityContextService;
    private final BatchSaveService batchSaveService;
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

        movementRepository.save(movement);

        item.setStockQuantity(quantityAfter);
        itemRepository.save(item);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.inventory.update.success"));
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
                InventoryItemEntity item = recipeIngredient.getInventoryItem();
                BigDecimal quantityToDecrease = recipeIngredient.getQuantityRequired()
                        .multiply(BigDecimal.valueOf(quantityOrdered));

                BigDecimal stockBefore = item.getStockQuantity();
                BigDecimal stockAfter = stockBefore.subtract(quantityToDecrease);

                item.setStockQuantity(stockAfter);
                item.setLastUpdated(Instant.now());

                InventoryMovementEntity movement = new InventoryMovementEntity();
                movement.setInventoryItem(item);
                movement.setMovementType(MovementTypeEnum.OUT);
                movement.setReason(MovementReasonEnum.RECIPE_USAGE);
                movement.setQuantityChanged(quantityToDecrease);
                movement.setQuantityBefore(stockBefore);
                movement.setQuantityAfter(stockAfter);
                movement.setUnitCostAtTime(item.getUnitCost());
                movement.setMovementDate(Instant.now());
                movement.setUser(order.getUser());
                movement.setReferenceId(order.getId());
                movement.setReferenceType(ReferenceTypeEnum.ORDER);

                movements.add(movement);
            }
        }

        movementRepository.saveAll(movements);
    }

}
