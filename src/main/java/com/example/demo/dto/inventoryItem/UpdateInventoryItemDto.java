package com.example.demo.dto.inventoryItem;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UpdateInventoryItemDto {
    private String name;
    private String unit;

    @Positive(message = "{validation.inventory.item.unitCost.positive}")
    private BigDecimal unitCost;

    @Positive(message = "{validation.inventory.item.minStockLevel.positive}")
    private BigDecimal minStockLevel;

    private Integer supplierId;
    private Integer categoryId;
    private String location;
    private Instant expiryDate;
    private Boolean isActive;
}