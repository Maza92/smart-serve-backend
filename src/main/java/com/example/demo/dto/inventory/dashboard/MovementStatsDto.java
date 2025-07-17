package com.example.demo.dto.inventory.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovementStatsDto {
    private final Integer month;
    private final Integer year;
    private final String reason;
    private final Long count;
    private final Double totalQuantityChanged;

    public MovementStatsDto(Integer month, Integer year, String reason, Long count, Double totalQuantityChanged) {
        this.month = month;
        this.year = year;
        this.reason = reason;
        this.count = count;
        this.totalQuantityChanged = totalQuantityChanged;
    }
}
