package com.example.demo.dto.cashMovement;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.enums.CashMovementTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashMovementDto {
    private Integer id;
    private Integer cashRegisterId;
    private String cashRegisterStatus;
    private String username;
    private BigDecimal amount;
    private CashMovementTypeEnum movementType;
    private String reason;
    private Instant movementDate;
    private String authorizedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean active;
}