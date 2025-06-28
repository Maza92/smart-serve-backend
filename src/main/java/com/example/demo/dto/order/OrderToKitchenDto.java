package com.example.demo.dto.order;

import java.time.Instant;
import java.util.List;

import com.example.demo.dto.orderDetail.OrderDetailToKitchenDto;
import com.example.demo.enums.OrderStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderToKitchenDto {
    private Integer id;
    private Integer tableNumber;
    private OrderStatusEnum status;
    private String customerName;
    private String waiterName;
    private Instant sentToKitchenAt;
    private List<OrderDetailToKitchenDto> orderDetails;
}
