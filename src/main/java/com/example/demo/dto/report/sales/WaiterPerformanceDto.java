package com.example.demo.dto.report.sales;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaiterPerformanceDto {
    private Long waiterId;
    private String firstName;
    private String lastName;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageTicket;
    private String profileImagePath;
}
