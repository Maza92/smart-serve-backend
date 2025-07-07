package com.example.demo.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.example.demo.dto.orderDetail.OrderDetailsDto;
import com.example.demo.enums.OrderServiceTypeEnum;
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
    private Integer tableNumber;
    private OrderStatusEnum status;
    private String comments;
    private OrderServiceTypeEnum serviceType;
    private BigDecimal totalPrice;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private String customerName;
    private Integer guestsCount;
    private Integer tableId;
    private Instant createdAt;
    List<OrderDetailsDto> orderDetails;
}
