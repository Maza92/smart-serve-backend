package com.example.demo.dto.order;

import java.util.List;

import com.example.demo.dto.orderDetail.CreateOrderDetailDto;
import com.example.demo.enums.OrderStatusEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class UpdateOrderWithDetailsDto {
    @NotBlank(message = "{validation.order.customerName.notBlank}")
    @Size(min = 2, max = 100, message = "{validation.order.customerName.size}")
    private String customerName;

    @NotEmpty(message = "{validation.order.details.notEmpty}")
    @Valid
    List<CreateOrderDetailDto> details;
}
