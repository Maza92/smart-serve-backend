package com.example.demo.dto.inventory;

import java.math.BigDecimal;

import com.example.demo.enums.MovementReasonEnum;
import com.example.demo.enums.MovementTypeEnum;
import com.example.demo.enums.ReferenceTypeEnum;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class UpdateInventoryItemStockDto {
    @NotNull(message = "{validation.inventory.itemId.required}")
    private Integer itemId;

    @NotNull(message = "{validation.inventory.movementType.required}")
    private MovementTypeEnum movementType;

    @NotNull(message = "{validation.inventory.quantityChanged.required}")
    @DecimalMin(value = "0.01", message = "{validation.inventory.quantityChanged.min}")
    private BigDecimal quantityChanged;

    @NotNull(message = "{validation.inventory.unitCostAtTime.required}")
    @DecimalMin(value = "0.00", message = "{validation.inventory.unitCostAtTime.min}")
    private BigDecimal unitCostAtTime;

    @NotNull(message = "{validation.inventory.reason.required}")
    private MovementReasonEnum reason;

    private Integer referenceId;

    private ReferenceTypeEnum referenceType;

    private String notes;
}
