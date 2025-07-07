package com.example.demo.dto.cashMovement;

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
public class CashMovementFilterDto {
    private Integer cashRegisterId;
    private Integer userId;
    private CashMovementTypeEnum movementType;
    private Instant startDate;
    private Instant endDate;
    private Boolean active;
}