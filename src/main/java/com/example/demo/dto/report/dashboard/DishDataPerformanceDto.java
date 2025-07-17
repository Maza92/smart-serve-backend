package com.example.demo.dto.report.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishDataPerformanceDto {
    private String dishName;
    private Long totalQuantity;
}
