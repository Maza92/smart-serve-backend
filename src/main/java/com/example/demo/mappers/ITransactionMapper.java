package com.example.demo.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.transaction.CreateOrderTransactionDto;
import com.example.demo.dto.transaction.CreateTransactionDto;
import com.example.demo.dto.transaction.TransactionDto;
import com.example.demo.entity.TransactionEntity;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = DateMapper.class)
public interface ITransactionMapper {
    ITransactionMapper INSTANCE = Mappers.getMapper(ITransactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cashRegister.id", source = "cashRegisterId")
    @Mapping(target = "order.id", source = "orderId")
    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(target = "active", constant = "true")
    TransactionEntity toEntity(CreateTransactionDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order.id", source = "orderId")
    @Mapping(target = "transactionType", constant = "SALE")
    @Mapping(target = "status", constant = "COMPLETED")
    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(target = "active", constant = "true")
    TransactionEntity toEntityFromOrderTransaction(CreateOrderTransactionDto dto);

    @Mapping(target = "cashRegisterId", source = "cashRegister.id")
    @Mapping(target = "cashRegisterStatus", source = "cashRegister.status")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderNumber", source = "order.id")
    @Mapping(target = "username", source = "user.username")
    TransactionDto toDto(TransactionEntity entity);
}