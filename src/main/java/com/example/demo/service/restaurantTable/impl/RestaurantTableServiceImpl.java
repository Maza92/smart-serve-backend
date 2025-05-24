package com.example.demo.service.restaurantTable.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.restaurantTable.CreateRestaurantTableDto;
import com.example.demo.dto.restaurantTable.RestaurantTableDto;
import com.example.demo.dto.restaurantTable.UpdateRestaurantTableDto;
import com.example.demo.entity.RestaurantTableEntity;
import com.example.demo.enums.RestaurantTableEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.ITableRestaurantMapper;
import com.example.demo.repository.RestaurantTableRepository;
import com.example.demo.service.restaurantTable.IRestaurantTableService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements IRestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final ApiExceptionFactory apiExceptionFactory;
    private final MessageUtils messageUtils;
    private final ITableRestaurantMapper tableRestaurantMapper;

    @Override
    public List<RestaurantTableEntity> getAllRestaurantTables() {
        return restaurantTableRepository.findAll();
    }

    public ApiSuccessDto<PageDto<RestaurantTableDto>> getAllRestaurantTables(int page, int size) {
        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Pageable pageable = Pageable.ofSize(size).withPage(Math.max(page - 1, 0));
        Page<RestaurantTableEntity> restaurantTables = restaurantTableRepository.findAll(pageable);

        List<RestaurantTableDto> restaurantTablesDto = restaurantTables.getContent().stream()
                .map(tableRestaurantMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.restaurant.table.get.all.success"),
                PageDto.fromPage(restaurantTables, restaurantTablesDto));
    }

    public ApiSuccessDto<Void> createRestaurantTable(CreateRestaurantTableDto restaurantTableEntity) {
        RestaurantTableEntity newRestaurantTable = new RestaurantTableEntity()
                .setNumber(restaurantTableEntity.getNumber())
                .setCapacity(restaurantTableEntity.getCapacity())
                .setStatus(RestaurantTableEnum.valueOf(restaurantTableEntity.getStatus()))
                .setSection(restaurantTableEntity.getSection());

        restaurantTableRepository.save(newRestaurantTable);
        return ApiSuccessDto.of(HttpStatus.CREATED.value(),
                messageUtils.getMessage("operation.restaurant.table.created"),
                null);
    }

    public ApiSuccessDto<RestaurantTableDto> getRestaurantTable(Integer id) {
        RestaurantTableEntity restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.restaurant.table.not.found"));

        RestaurantTableDto dto = tableRestaurantMapper.toDto(restaurantTable);
        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.restaurant.table.get.success"),
                dto);
    }

    public ApiSuccessDto<Void> updateRestaurantTable(UpdateRestaurantTableDto restaurantTableEntity, Integer id) {

        RestaurantTableEntity restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.restaurant.table.not.found"));

        tableRestaurantMapper.update(restaurantTable, restaurantTableEntity);
        restaurantTableRepository.save(restaurantTable);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.restaurant.table.updated"),
                null);
    }
}