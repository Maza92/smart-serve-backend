package com.example.demo.dto.inventory.dashboard;

import java.math.BigDecimal;

import com.example.demo.enums.TrendTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TotalItemsMetricDto {
    private Integer currentCount;
    private Double percentageChange;
    private TrendTypeEnum trend;
    private String changeDescription;
}
