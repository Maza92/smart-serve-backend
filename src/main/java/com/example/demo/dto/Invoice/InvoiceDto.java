package com.example.demo.dto.Invoice;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InvoiceDto {
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantTaxId;
    private String invoiceNumber;
    private Instant issuedAt;
    private Integer orderId;
    private String customerName;
    private Integer tableNumber;
    private String waiterName;
    private Integer guestsCount;
    private List<InvoiceDetailDto> details;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalPrice;
}
