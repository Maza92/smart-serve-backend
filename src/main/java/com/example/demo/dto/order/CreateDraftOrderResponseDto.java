package com.example.demo.dto.order;

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
public class CreateDraftOrderResponseDto {
    private Integer orderId;
    private OrderStatusEnum status;
    private Integer tableId;
    private Instant createdAt;
}
