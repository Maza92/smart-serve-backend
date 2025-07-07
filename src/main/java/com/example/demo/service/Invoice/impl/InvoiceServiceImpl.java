package com.example.demo.service.Invoice.impl;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Invoice.InvoiceDto;
import com.example.demo.entity.OrderEntity;
import com.example.demo.mappers.IInvoiceMapper;
import com.example.demo.service.Invoice.IInvoiceService;
import com.example.demo.service.parameter.IParameterService;

import lombok.RequiredArgsConstructor;

// TODO: Simulate invoice generation
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

    private final IParameterService parameterService;
    private final IInvoiceMapper invoiceMapper;

    @Override
    public InvoiceDto convertOrderToInvoice(OrderEntity order) {
        InvoiceDto invoice = invoiceMapper.toDto(order);
        invoice.setRestaurantName(parameterService.getString("RESTAURANT_NAME"));
        invoice.setRestaurantAddress(parameterService.getString("RESTAURANT_ADDRESS"));
        invoice.setRestaurantTaxId(parameterService.getString("RESTAURANT_TAX_ID"));
        invoice.setInvoiceNumber(generateInvoiceNumber(order.getId()));
        invoice.setTaxAmount(order.getSubtotal().subtract(order.getDiscountAmount()));
        return invoice;
    }

    private String generateInvoiceNumber(Integer orderId) {
        String series = "B001";
        return String.format("%s-%06d", series, orderId);
    }

}
