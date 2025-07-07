package com.example.demo.service.cashRegister.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;
import com.example.demo.dto.cashRegister.CashRegisterDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;
import com.example.demo.entity.CashRegisterEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.CashRegisterEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.ICashRegisterMapper;
import com.example.demo.repository.CashRegisterRepository;
import com.example.demo.service.cashRegister.ICashRegisterService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CashRegisterServiceImpl implements ICashRegisterService {

    private final CashRegisterRepository cashRegisterRepository;
    private final ISecurityContextService securityContextService;
    private final ApiExceptionFactory apiExceptionFactory;
    private final MessageUtils messageUtils;

    private final ICashRegisterMapper cashRegisterMapper;

    @Override
    public ApiSuccessDto<Void> createPartialCashRegister(PartialCreateCashRegisterDto cashRegister) {

        if (cashRegisterRepository.existsOpenedCashRegister()) {
            throw apiExceptionFactory.badRequestException("operation.cash.register.already.opened");
        }

        long createdToday = cashRegisterRepository.countCreatedToday();
        if (createdToday >= 3) {
            throw apiExceptionFactory.badRequestException("operation.cash.register.max.created.per.day");
        }

        CashRegisterEntity newCashRegister = new CashRegisterEntity()
                .setUser(new UserEntity()
                        .setId(securityContextService.getSubjectAsInt()))
                .setCloseDate(null)
                .setInitialAmount(BigDecimal.ZERO)
                .setFinalAmount(BigDecimal.ZERO)
                .setExpectedAmount(BigDecimal.ZERO)
                .setDifference(BigDecimal.ZERO)
                .setNotes(cashRegister.getNotes());

        cashRegisterRepository.save(newCashRegister);
        return ApiSuccessDto.of(HttpStatus.CREATED.value(), messageUtils.getMessage("operation.cash.register.created"),
                null);
    }

    @Override
    public ApiSuccessDto<Void> OpenCashRegister(OpenCashRegisterDto cashRegister, Integer id) {

        if (cashRegisterRepository.existsOpenedCashRegister()) {
            throw apiExceptionFactory.badRequestException("operation.cash.register.already.opened");
        }

        CashRegisterEntity existingCashRegister = cashRegisterRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.register.not.found"));

        if (existingCashRegister.getCloseDate() != null) {
            throw apiExceptionFactory.badRequestException("operation.cash.register.closed");
        }

        cashRegisterMapper.updateToOpen(existingCashRegister, cashRegister);
        existingCashRegister.setStatus(CashRegisterEnum.OPENED);
        existingCashRegister.setOpenDate(Instant.now());

        cashRegisterRepository.save(existingCashRegister);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.cash.register.updated"),
                null);
    }

    public ApiSuccessDto<Void> CloseCashRegister(ClosedCashRegisterDto cashRegister, Integer id) {
        CashRegisterEntity existingCashRegister = cashRegisterRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.register.not.found"));

        if (existingCashRegister.getCloseDate() != null) {
            throw apiExceptionFactory.badRequestException("operation.cash.register.closed");
        }

        cashRegisterMapper.updateToClose(existingCashRegister, cashRegister);
        existingCashRegister.setStatus(CashRegisterEnum.CLOSED);
        existingCashRegister.setCloseDate(Instant.now());

        cashRegisterRepository.save(existingCashRegister);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.cash.register.updated"),
                null);
    }

    @Override
    public ApiSuccessDto<PageDto<CashRegisterDto>> getAllCashRegisters(int page, int size, String sortDirection,
            String sortBy) {
        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);
        Page<CashRegisterEntity> cashRegisters = cashRegisterRepository.findAll(pageable);

        List<CashRegisterDto> cashRegistersDto = cashRegisters.getContent().stream()
                .map(cashRegisterMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.register.get.all.success"),
                PageDto.fromPage(cashRegisters, cashRegistersDto));
    }

    @Override
    public ApiSuccessDto<CashRegisterEnum> getCashRegisterStatus() {
        List<CashRegisterEntity> openedToday = cashRegisterRepository.findOpenedToday();
        if (!openedToday.isEmpty()) {
            return ApiSuccessDto.of(HttpStatus.OK.value(),
                    messageUtils.getMessage("operation.cash.register.get.status.success"),
                    CashRegisterEnum.OPENED);
        }

        List<CashRegisterEntity> createdToday = cashRegisterRepository.findCreatedToday();
        if (!createdToday.isEmpty()) {
            return ApiSuccessDto.of(HttpStatus.OK.value(),
                    messageUtils.getMessage("operation.cash.register.get.status.created"),
                    CashRegisterEnum.CREATED);
        }

        List<CashRegisterEntity> closedToday = cashRegisterRepository.findClosedToday();
        if (!closedToday.isEmpty()) {
            return ApiSuccessDto.of(HttpStatus.OK.value(),
                    messageUtils.getMessage("operation.cash.register.get.status.success"),
                    CashRegisterEnum.CLOSED);
        }

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.register.get.status.none"),
                CashRegisterEnum.NONE);
    }

    @Override
    public ApiSuccessDto<CashRegisterDto> getCurrentOpenedCashRegister() {
        Optional<CashRegisterEntity> openedRegister = cashRegisterRepository.findCurrentOpenedCashRegister();

        if (openedRegister.isEmpty()) {
            throw apiExceptionFactory.entityNotFound("operation.cash.register.no.opened.found");
        }

        CashRegisterDto dto = cashRegisterMapper.toDto(openedRegister.get());
        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.register.current.found"),
                dto);
    }

    @Override
    public ApiSuccessDto<List<CashRegisterDto>> getAvailableCashRegistersToOpen() {
        List<CashRegisterEntity> availableRegisters = cashRegisterRepository.findAvailableCashRegistersToOpen();

        if (availableRegisters.isEmpty()) {
            return ApiSuccessDto.of(HttpStatus.OK.value(),
                    messageUtils.getMessage("operation.cash.register.no.available"),
                    new ArrayList<>());
        }

        List<CashRegisterDto> dtos = availableRegisters.stream()
                .map(cashRegisterMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.register.available.found"),
                dtos);
    }

    @Override
    public CashRegisterEntity validateCashRegister() {
        CashRegisterEntity currentCashRegister = cashRegisterRepository.findCurrentOpenedCashRegister()
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.register.no.opened.found"));

        if (currentCashRegister.getStatus() != CashRegisterEnum.OPENED) {
            throw apiExceptionFactory.badRequestException("operation.cash.register.not.opened");
        }
        return currentCashRegister;
    }

}