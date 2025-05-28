package com.example.demo.dto.inventoryItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateInventoryItemDto {
    @NotBlank(message = "{validation.inventory.item.name.notBlank}")
    private String name;

    @NotBlank(message = "{validation.inventory.item.unit.notBlank}")
    private String unit;

    @NotNull(message = "{validation.inventory.item.unitCost.notNull}")
    @Positive(message = "{validation.inventory.item.unitCost.positive}")
    private BigDecimal unitCost;

    @NotNull(message = "{validation.inventory.item.minStockLevel.notNull}")
    @Positive(message = "{validation.inventory.item.minStockLevel.positive}")
    private BigDecimal minStockLevel;

    @NotNull(message = "{validation.inventory.item.supplierId.notNull}")
    private Integer supplierId;

    private String location;
    private LocalDateTime expiryDate;
    private Boolean isActive = true;
}