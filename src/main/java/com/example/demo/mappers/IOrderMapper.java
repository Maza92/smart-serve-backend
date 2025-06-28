package com.example.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.order.CreateDraftOrderResponseDto;
import com.example.demo.dto.order.OrderToKitchenDto;
import com.example.demo.dto.order.UpdateOrderToKitchenDto;
import com.example.demo.dto.order.UpdateOrderWithDetailsResponseDto;
import com.example.demo.entity.OrderEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = OrderDetailMapper.class)
public interface IOrderMapper {
    IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "tableId", source = "table.id")
    CreateDraftOrderResponseDto toDraftOrderResponseDto(OrderEntity order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "tableId", source = "table.id")
    @Mapping(target = "orderDetails", source = "orderDetails")
    UpdateOrderWithDetailsResponseDto toUpdateOrderWithDetailsResponseDto(OrderEntity order);

    @Mapping(target = "tableNumber", source = "table.number")
    @Mapping(target = "waiterName", source = "user.username")
    @Mapping(target = "sentToKitchenAt", source = "updatedAt")
    @Mapping(target = "orderDetails", source = "orderDetails")
    OrderToKitchenDto toOrderToKitchenDto(OrderEntity order);

    UpdateOrderToKitchenDto toUpdateOrderToKitchenDto(OrderEntity order);
}
