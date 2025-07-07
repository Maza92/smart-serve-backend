package com.example.demo.dto.report.dashboard;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesDto {
    private Instant date;
    private BigDecimal totalSales;
    private Long orderCount;
}
