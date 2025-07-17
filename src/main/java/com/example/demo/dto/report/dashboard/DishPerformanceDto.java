package com.example.demo.dto.report.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishPerformanceDto {
    private List<String> dishNames;
    private List<Integer> quantities;
    private Integer totalDishesAnalyzed;
    private Boolean isLimited;
}
