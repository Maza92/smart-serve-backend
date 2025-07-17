package com.example.demo.dto.orderDetail;

import java.math.BigDecimal;

import com.example.demo.enums.ModificationTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ModificationDto {
    private Integer inventoryItemId;
    private String ingredientName;
    private ModificationTypeEnum action;
    private BigDecimal quantityChange;
    private Integer unitId;
    private BigDecimal priceAdjustment;
}
