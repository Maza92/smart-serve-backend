package com.example.demo.dto.report.sales;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportFiltersDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long categoryId;
    private Long waiterId;
    private String serviceType;
    private String temporalGrouping;
    private String orderStatus;
    private Long tableNumber;
}
