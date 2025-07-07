package com.example.demo.dto.orderDetail;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderDetailsDto {
    private Integer id;
    private Integer dishId;
    private String dishName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal finalPrice;
    private String status;
    private Instant preparedAt;
}
