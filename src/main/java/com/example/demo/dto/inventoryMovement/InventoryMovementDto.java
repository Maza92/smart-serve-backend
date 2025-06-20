package com.example.demo.dto.inventoryMovement;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.enums.MovementReasonEnum;
import com.example.demo.enums.MovementTypeEnum;
import com.example.demo.enums.ReferenceTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
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
public class InventoryMovementDto {
    private Integer itemId;
    private String itemName;
    private Integer userId;
    private MovementTypeEnum movementType;
    private BigDecimal quantityBefore;
    private BigDecimal quantityAfter;
    private BigDecimal quantityChanged;
    private BigDecimal unitCostAtTime;
    private MovementReasonEnum reason;
    private Integer referenceId;
    private ReferenceTypeEnum referenceType;
    private String notes;
    private Instant movementDate;
}
