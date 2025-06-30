package com.example.demo.dto.orderDetail;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderDetailToKitchenDto {
    private Integer id;
    private Integer quantity;
    private String dishName;
    private String categoryName;
    private String estimatedPreparationTime;
    private List<ModificationDto> modifications;
}
