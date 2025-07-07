package com.example.demo.service.cashMovement.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashMovement.CashMovementDto;
import com.example.demo.dto.cashMovement.CreateCashMovementDto;
import com.example.demo.entity.CashMovementEntity;
import com.example.demo.entity.CashRegisterEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.CashMovementTypeEnum;
import com.example.demo.enums.CashRegisterEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.ICashMovementMapper;
import com.example.demo.repository.CashMovementRepository;
import com.example.demo.repository.CashRegisterRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.cashMovement.ICashMovementService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.specifications.CashMovementSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashMovementServiceImpl implements ICashMovementService {

    private final CashMovementRepository cashMovementRepository;
    private final CashRegisterRepository cashRegisterRepository;
    private final ICashMovementMapper cashMovementMapper;
    private final ApiExceptionFactory apiExceptionFactory;
    private final ISecurityContextService securityContextService;
    private final MessageUtils messageUtils;

    @Override
    @Transactional
    public ApiSuccessDto<CashMovementDto> createCashMovement(CreateCashMovementDto createCashMovementDto) {
        if (!cashRegisterRepository.existsOpenedCashRegister()) {
            throw apiExceptionFactory.badRequestException("operation.cash.movement.no.open.register");
        }

        CashRegisterEntity cashRegister = cashRegisterRepository.findCurrentOpenedCashRegister()
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.register.not.found"));

        if (!cashRegister.getId().equals(createCashMovementDto.getCashRegisterId())) {
            throw apiExceptionFactory.badRequestException("operation.cash.movement.invalid.register");
        }

        UserEntity user = securityContextService.getUser();

        CashMovementEntity cashMovement = cashMovementMapper.toEntity(createCashMovementDto);
        cashMovement.setCashRegister(cashRegister);
        cashMovement.setUser(user);
        cashMovement.setMovementDate(Instant.now());
        cashMovement.setActive(true);

        updateCashRegisterBalance(cashRegister, createCashMovementDto.getAmount(),
                createCashMovementDto.getMovementType());

        CashMovementEntity savedCashMovement = cashMovementRepository.save(cashMovement);

        cashRegisterRepository.save(cashRegister);

        return ApiSuccessDto.of(HttpStatus.CREATED.value(),
                messageUtils.getMessage("operation.cash.movement.created"),
                cashMovementMapper.toDto(savedCashMovement));
    }

    @Override
    public ApiSuccessDto<PageDto<CashMovementDto>> getCashMovements(int page, int size, Integer cashRegisterId,
            Integer userId, CashMovementTypeEnum movementType, String startDate, String endDate, String sortBy,
            String sortDirection) {

        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

        Specification<CashMovementEntity> spec = Specification.where(CashMovementSpecifications.activeEquals(true));

        if (cashRegisterId != null) {
            spec = spec.and(CashMovementSpecifications.cashRegisterIdEquals(cashRegisterId));
        }

        if (userId != null) {
            spec = spec.and(CashMovementSpecifications.userIdEquals(userId));
        }

        if (movementType != null) {
            spec = spec.and(CashMovementSpecifications.movementTypeEquals(movementType));
        }

        Instant startInstant = null;
        Instant endInstant = null;

        if (startDate != null && !startDate.isBlank()) {
            startInstant = Instant.parse(startDate);
        }

        if (endDate != null && !endDate.isBlank()) {
            endInstant = Instant.parse(endDate);
        }

        if (startInstant != null || endInstant != null) {
            spec = spec.and(CashMovementSpecifications.dateRangeBetween(startInstant, endInstant));
        }

        Page<CashMovementEntity> cashMovements = cashMovementRepository.findAll(spec, pageable);

        List<CashMovementDto> cashMovementDtos = cashMovements.getContent().stream()
                .map(cashMovementMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.movement.get.all.success"),
                PageDto.fromPage(cashMovements, cashMovementDtos));
    }

    @Override
    public ApiSuccessDto<CashMovementDto> getCashMovementById(Integer id) {
        CashMovementEntity cashMovement = cashMovementRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.movement.not.found"));

        if (!cashMovement.getActive()) {
            throw apiExceptionFactory.entityNotFound("operation.cash.movement.not.found");
        }

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.movement.get.by.id.success"),
                cashMovementMapper.toDto(cashMovement));
    }

    @Override
    @Transactional
    public ApiSuccessDto<Void> deleteCashMovement(Integer id) {
        CashMovementEntity cashMovement = cashMovementRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.cash.movement.not.found"));

        if (!cashMovement.getActive()) {
            throw apiExceptionFactory.entityNotFound("operation.cash.movement.not.found");
        }

        CashRegisterEntity cashRegister = cashMovement.getCashRegister();
        if (cashRegister.getStatus() != CashRegisterEnum.OPENED) {
            throw apiExceptionFactory.badRequestException("operation.cash.movement.register.closed");
        }

        reverseCashRegisterBalance(cashRegister, cashMovement);

        cashMovement.setActive(false);
        cashMovementRepository.save(cashMovement);

        cashRegisterRepository.save(cashRegister);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.movement.deleted"),
                null);
    }

    private void updateCashRegisterBalance(CashRegisterEntity cashRegister, java.math.BigDecimal amount,
            CashMovementTypeEnum movementType) {
        switch (movementType) {
            case INCOME, ADJUSTMENT_INCOME, REFUND -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().add(amount));
            }
            case EXPENSE, ADJUSTMENT_EXPENSE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().subtract(amount));
            }
            case SALE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().add(amount));
            }
            default -> {
                log.warn("Movement type not found: " + movementType);
            }
        }

        cashRegister.setDifference(cashRegister.getExpectedAmount().subtract(cashRegister.getFinalAmount()));
    }

    private void reverseCashRegisterBalance(CashRegisterEntity cashRegister, CashMovementEntity cashMovement) {
        CashMovementTypeEnum movementType = cashMovement.getMovementType();
        java.math.BigDecimal amount = cashMovement.getAmount();

        switch (movementType) {
            case INCOME, ADJUSTMENT_INCOME, REFUND -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().subtract(amount));
            }
            case EXPENSE, ADJUSTMENT_EXPENSE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().add(amount));
            }
            case SALE -> {
                cashRegister.setExpectedAmount(cashRegister.getExpectedAmount().subtract(amount));
            }
            default -> {
                log.warn("Movement type not found: " + movementType);
            }
        }

        cashRegister.setDifference(cashRegister.getExpectedAmount().subtract(cashRegister.getFinalAmount()));
    }
}