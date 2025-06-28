package com.example.demo.dto.order;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.enums.OrderStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderDto {
    private Integer id;
    private Integer userId;
    private String userName;
    private OrderStatusEnum status;
    private BigDecimal totalPrice;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private String customerName;
    private Integer guestsCount;
    private Integer tableId;
    private Instant createdAt;

}
