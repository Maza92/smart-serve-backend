package com.example.demo.dto.Invoice;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InvoiceDetailDto {
    private Integer quantity;
    private String dishName;
    private BigDecimal unitPrice;
    private BigDecimal finalPrice;
}
