package com.example.demo.dto.orderDetail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderDetailsReponseDto {
    private Integer dishId;
    private String modifications;
}
