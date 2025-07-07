package com.example.demo.dto.report.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesSummaryDto {
    private Long totalTransactions;
    private BigDecimal totalRevenue;
    private BigDecimal averageTicket;
}
