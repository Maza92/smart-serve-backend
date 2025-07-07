package com.example.demo.dto.order;

import java.math.BigDecimal;

import com.example.demo.enums.TrendTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TotalSalesDto {
    BigDecimal totalSales;
    TrendTypeEnum trendType;
    long todaySales;
}
