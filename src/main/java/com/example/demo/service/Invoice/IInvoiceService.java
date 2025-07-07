package com.example.demo.service.Invoice;

import com.example.demo.dto.Invoice.InvoiceDto;
import com.example.demo.entity.OrderEntity;

public interface IInvoiceService {

    InvoiceDto convertOrderToInvoice(OrderEntity order);

}
