package com.example.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.example.demo.dto.orderDetail.OrderDetailToKitchenDto;
import com.example.demo.dto.orderDetail.OrderDetailsReponseDto;
import com.example.demo.entity.OrderDetailEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderDetailMapper {

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "modifications", source = "modifications", qualifiedByName = "jsonNodeToString")
    OrderDetailsReponseDto toOrderDetailsReponseDto(OrderDetailEntity orderDetailEntity);

    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "categoryName", source = "dish.category.name")
    @Mapping(target = "estimatedPreparationTime", source = "dish.preparationTime")
    @Mapping(target = "modifications", source = "modifications", qualifiedByName = "jsonNodeToString")
    OrderDetailToKitchenDto toOrderDetailToKitchenDto(OrderDetailEntity orderDetailEntity);

    @Named("jsonNodeToString")
    default String jsonNodeToString(JsonNode jsonNode) {
        return jsonNode != null ? jsonNode.toString() : null;
    }
}