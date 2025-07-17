package com.example.demo.dto.inventoryItem;

import java.math.BigDecimal;
import java.time.Instant;

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
    private String imagePath;
    private Integer unitId;
    private String unitName;
    private String unitAbbreviation;
    private BigDecimal stockQuantity;
    private BigDecimal unitCost;
    private BigDecimal minStockLevel;
    private String supplierName;
    private Integer supplierId;
    private Integer categoryId;
    private String location;
    private Instant lastUpdated;
    private Instant expiryDate;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}