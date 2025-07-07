package com.example.demo.mappers;

import java.io.IOException;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.example.demo.dto.orderDetail.ModificationDto;
import com.example.demo.dto.orderDetail.OrderDetailToKitchenDto;
import com.example.demo.dto.orderDetail.OrderDetailsDto;
import com.example.demo.dto.orderDetail.OrderDetailsReponseDto;
import com.example.demo.entity.OrderDetailEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderDetailMapper {

    @Mapping(target = "dishId", source = "dish.id")
    OrderDetailsReponseDto toOrderDetailsReponseDto(OrderDetailEntity orderDetailEntity);

    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "categoryName", source = "dish.category.name")
    @Mapping(target = "estimatedPreparationTime", source = "dish.preparationTime")
    @Mapping(target = "modifications", source = "modifications", qualifiedByName = "jsonNodeToModificationList")
    OrderDetailToKitchenDto toOrderDetailToKitchenDto(OrderDetailEntity orderDetailEntity);

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishName", source = "dish.name")
    OrderDetailsDto toOrderDetailsDto(OrderDetailEntity orderDetailEntity);

    List<OrderDetailsDto> toOrderDetailsDto(List<OrderDetailEntity> orderDetailEntities);

    @Named("jsonNodeToModificationList")
    default List<ModificationDto> jsonNodeToModificationList(JsonNode jsonNode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readerForListOf(ModificationDto.class).readValue(jsonNode);
    }

}