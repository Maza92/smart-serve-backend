package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.Invoice.InvoiceDetailDto;
import com.example.demo.dto.Invoice.InvoiceDto;
import com.example.demo.entity.OrderDetailEntity;
import com.example.demo.entity.OrderEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface IInvoiceMapper {
    IInvoiceMapper INSTANCE = Mappers.getMapper(IInvoiceMapper.class);

    @Mapping(target = "issuedAt", source = "createdAt")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "tableNumber", source = "table.number")
    @Mapping(target = "waiterName", source = "user.username")
    @Mapping(target = "guestsCount", source = "guestsCount")
    @Mapping(target = "details", source = "orderDetails")
    @Mapping(target = "subtotal", source = "subtotal")
    @Mapping(target = "discountAmount", source = "discountAmount")
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "totalPrice", source = "totalPrice")
    InvoiceDto toDto(OrderEntity order);

    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "finalPrice", source = "finalPrice")
    InvoiceDetailDto toDetailDto(OrderDetailEntity orderDetail);

    List<InvoiceDetailDto> toDetailDto(List<OrderDetailEntity> orderDetails);
}
