package com.example.demo.dto.inventoryItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class InventoryItemDto {
    private Integer id;
    private String name;
    private String unit;
    private BigDecimal stockQuantity;
    private BigDecimal unitCost;
    private BigDecimal minStockLevel;
    private String supplierName;
    private Integer supplierId;
    private String location;
    private LocalDateTime lastUpdated;
    private LocalDateTime expiryDate;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}