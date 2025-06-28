package com.example.demo.dto.orderDetail;

import java.math.BigDecimal;
import java.time.Instant;

public class OrdenDetailsDto {
    private Integer id;
    private Integer orderId;
    private Integer dishId;
    private String modifications;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal finalPrice;
    private String status;
    private Instant preparedAt;
}
