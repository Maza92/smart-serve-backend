package com.example.demo.service.cashRegister.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Creates a minimal cash register entry with just the notes information.
     * 
     * This method performs the following operations:
     * <ul>
     * <li>Creates a new CashRegisterEntity with minimal data</li>
     * <li>Sets the current user as the owner</li>
     * <li>Initializes all amounts to zero</li>
     * <li>Only requires notes from the DTO</li>
     * <li>Saves the new minimal cash register entry to the repository</li>
     * </ul>
     * 
     * @param cashRegister
     *                     The DTO containing only the notes required for minimal
     *                     creation
     * @return An ApiSuccessDto with the creation status
     */
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

    /**
     * Opens a cash register operation with the provided opening information.
     * 
     * This method performs the following operations:
     * <ul>
     * <li>Verifies that the cash register exists in the system</li>
     * <li>Checks that the cash register is not already closed</li>
     * <li>Updates the cash register with opening information from the DTO</li>
     * <li>Sets the status to OPENED and records the opening timestamp</li>
     * <li>Saves the updated cash register to the repository</li>
     * </ul>
     * 
     * @param cashRegister
     *                     The DTO containing data required to open the cash
     *                     register
     * @param id
     *                     The unique identifier of the cash register to open
     * @return An ApiSuccessDto containing the operation result
     * @throws EntityNotFoundException
     *                                 if the cash register with the given ID
     *                                 doesn't exist
     * @throws BadRequestException
     *                                 if the cash register is already closed
     */
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

    /**
     * Closes a cash register operation with the provided closing information.
     * 
     * This method performs the following operations:
     * <ul>
     * <li>Verifies that the cash register exists in the system</li>
     * <li>Checks that the cash register is not already closed</li>
     * <li>Updates the cash register with closing information from the DTO</li>
     * <li>Sets the status to CLOSED and records the closing timestamp</li>
     * <li>Saves the updated cash register to the repository</li>
     * </ul>
     * 
     * <p>
     * Future enhancement: The system will calculate the expected amount and the
     * difference:
     * </p>
     * 
     * <pre>
     *   expectedAmount = initialAmount + totalCashSales + incomes - expenses
     *   difference = expectedAmount - finalAmount
     * </pre>
     * 
     * @param cashRegister
     *                     The DTO containing data required to close the cash
     *                     register
     * @param id
     *                     The unique identifier of the cash register to close
     * @return An ApiSuccessDto containing the operation result
     * @throws EntityNotFoundException
     *                                 if the cash register with the given ID
     *                                 doesn't exist
     * @throws BadRequestException
     *                                 if the cash register is already closed
     */
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

    /**
     * Retrieves a paginated list of all cash registers in the system.
     * 
     * Validates the page and size parameters to ensure they are positive integers.
     * Converts the result to DTOs and wraps it in a pagination structure.
     * 
     * @param page the page number to retrieve (1-based)
     * @param size the number of items per page
     * @return a successful response containing a paginated list of cash registers
     * @throws BadRequestException if the page or size parameters are invalid
     */
    @Override
    public ApiSuccessDto<PageDto<CashRegisterDto>> getAllCashRegisters(int page, int size) {
        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Pageable pageable = Pageable.ofSize(size).withPage(Math.max(page - 1, 0));
        Page<CashRegisterEntity> cashRegisters = cashRegisterRepository.findAll(pageable);

        List<CashRegisterDto> cashRegistersDto = cashRegisters.getContent().stream()
                .map(cashRegisterMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.cash.register.get.all.success"),
                PageDto.fromPage(cashRegisters, cashRegistersDto));
    }

    /**
     * Retrieves the current status of today's cash register activity.
     * 
     * The method checks today's cash register records in the following order of
     * precedence:
     * <ol>
     * <li>If any register is OPENED, returns {@code CashRegisterEnum.OPENED}</li>
     * <li>If none are opened but at least one is CREATED, returns
     * {@code CashRegisterEnum.CREATED}</li>
     * <li>If none are created but at least one is CLOSED, returns
     * {@code CashRegisterEnum.CLOSED}</li>
     * <li>If no register activity is found for today, returns
     * {@code CashRegisterEnum.NONE}</li>
     * </ol>
     * 
     * @return an ApiSuccessDto containing the current cash register status for
     *         today
     */
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
}