package com.example.demo.dto.report.sales;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesChartDto {
    private List<String> labels;
    private List<BigDecimal> values;
    private List<Long> orderCounts;
}