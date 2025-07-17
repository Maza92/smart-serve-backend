package com.example.demo.dto.cashMovement;

import java.math.BigDecimal;

import com.example.demo.enums.CashMovementTypeEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCashMovementDto {
    @NotNull(message = "Cash register ID is required")
    private Integer cashRegisterId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Movement type is required")
    private CashMovementTypeEnum movementType;

    private String reason;

    private String authorizedBy;
}
