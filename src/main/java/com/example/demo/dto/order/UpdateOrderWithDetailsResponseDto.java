package com.example.demo.dto.order;

import java.util.List;

import com.example.demo.dto.orderDetail.OrderDetailsReponseDto;
import com.example.demo.enums.OrderStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UpdateOrderWithDetailsResponseDto {
    private Integer orderId;
    private OrderStatusEnum status;
    private Integer tableId;
    private List<OrderDetailsReponseDto> orderDetails;
}
