package com.example.demo.dto.orderDetail;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CreateOrderDetailDto {
    @NotNull(message = "{validation.order.detail.dishId.notNull}")
    @Positive(message = "{validation.order.detail.dishId.positive}")
    private Integer dishId;

    @NotNull(message = "{validation.order.detail.quantity.notNull}")
    @Positive(message = "{validation.order.detail.quantity.positive}")
    @Max(value = 50, message = "{validation.order.detail.quantity.max}")
    private Integer quantity;

    @Size(max = 500, message = "{validation.order.detail.modifications.size}")
    private String modifications;
}
