package com.example.demo.service.restaurantTable.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import com.example.demo.service.webSocket.IStompMessagingService;
import com.example.demo.specifications.RestaurantTableSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements IRestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final ApiExceptionFactory apiExceptionFactory;
    private final MessageUtils messageUtils;
    private final ITableRestaurantMapper tableRestaurantMapper;
    private final IStompMessagingService stompMessagingService;

    @Override
    public List<RestaurantTableEntity> getAllRestaurantTables() {
        return restaurantTableRepository.findAll();
    }

    @Override
    public ApiSuccessDto<PageDto<RestaurantTableDto>> getAllRestaurantTables(int page, int size, String number,
            String status, String section, String sortBy, String sortDirection) {
        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);
        Specification<RestaurantTableEntity> spec = Specification.where(null);

        if (number != null && !number.isBlank()) {
            spec = spec.and(RestaurantTableSpecifications.numberEquals(number));
        }

        if (status != null && !status.isBlank()) {
            spec = spec.and(RestaurantTableSpecifications.statusEquals(status));
        }

        if (section != null && !section.isBlank()) {
            spec = spec.and(RestaurantTableSpecifications.sectionEquals(section));
        }

        Page<RestaurantTableEntity> restaurantTables = restaurantTableRepository.findAll(spec, pageable);

        List<RestaurantTableDto> restaurantTablesDto = restaurantTables.getContent().stream()
                .map(tableRestaurantMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.restaurant.table.get.all.success"),
                PageDto.fromPage(restaurantTables, restaurantTablesDto));
    }

    @Override
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

    @Override
    public ApiSuccessDto<RestaurantTableDto> getRestaurantTable(Integer id) {
        RestaurantTableEntity restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.restaurant.table.not.found"));

        RestaurantTableDto dto = tableRestaurantMapper.toDto(restaurantTable);
        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.restaurant.table.get.success"),
                dto);
    }

    @Override
    public ApiSuccessDto<Void> updateRestaurantTable(UpdateRestaurantTableDto restaurantTableEntity, Integer id) {

        RestaurantTableEntity restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.restaurant.table.not.found"));

        tableRestaurantMapper.update(restaurantTable, restaurantTableEntity);
        restaurantTableRepository.save(restaurantTable);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.restaurant.table.updated"),
                null);
    }

    @Override
    public RestaurantTableEntity isRestaurantTableAvailable(Integer id) {
        RestaurantTableEntity restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.restaurant.table.not.found"));

        if (restaurantTable.getStatus() != RestaurantTableEnum.AVAILABLE)
            throw apiExceptionFactory.badRequestException("operation.restaurant.table.not.available");

        return restaurantTable;
    }

    @Override
    public RestaurantTableEntity setStatus(Integer id, RestaurantTableEnum status) {
        RestaurantTableEntity restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.restaurant.table.not.found"));

        return setStatus(restaurantTable, status);
    }

    @Override
    public RestaurantTableEntity setStatus(RestaurantTableEntity restaurantTable, RestaurantTableEnum status) {
        restaurantTable.setStatus(status);
        return restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public RestaurantTableEntity setStatusAndSendMessage(Integer id, RestaurantTableEnum status) {
        RestaurantTableEntity restaurantTable = setStatus(id, status);
        broadcastTableStatusChange(restaurantTable);
        return restaurantTable;
    }

    @Override
    public RestaurantTableEntity setStatusAndSendMessage(RestaurantTableEntity restaurantTable,
            RestaurantTableEnum status) {
        setStatus(restaurantTable, status);
        broadcastTableStatusChange(restaurantTable);
        return restaurantTable;
    }

    private void broadcastTableStatusChange(RestaurantTableEntity restaurantTable) {
        String destination = "/topic/tables";
        RestaurantTableDto dto = tableRestaurantMapper.toDto(restaurantTable);
        stompMessagingService.sendMessage(destination, dto);
    }

}