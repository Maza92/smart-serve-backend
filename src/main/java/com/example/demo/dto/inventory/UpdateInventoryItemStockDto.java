package com.example.demo.dto.inventory;

import java.math.BigDecimal;

import com.example.demo.enums.MovementReasonEnum;
import com.example.demo.enums.MovementTypeEnum;
import com.example.demo.enums.ReferenceTypeEnum;

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
    private Integer itemId;
    private MovementTypeEnum movementType;
    private BigDecimal quantityChanged;
    private BigDecimal unitCostAtTime;
    private MovementReasonEnum reason;
    private Integer referenceId;
    private ReferenceTypeEnum referenceType;
    private String note;
}
