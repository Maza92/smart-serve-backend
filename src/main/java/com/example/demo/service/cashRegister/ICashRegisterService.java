package com.example.demo.service.cashRegister;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;
import com.example.demo.entity.CashRegisterEntity;
import com.example.demo.enums.CashRegisterEnum;
import com.example.demo.dto.cashRegister.CashRegisterDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;

public interface ICashRegisterService {
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
    ApiSuccessDto<Void> createPartialCashRegister(PartialCreateCashRegisterDto cashRegister);

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
    ApiSuccessDto<Void> OpenCashRegister(OpenCashRegisterDto cashRegister, Integer id);

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
    ApiSuccessDto<Void> CloseCashRegister(ClosedCashRegisterDto cashRegister, Integer id);

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
    ApiSuccessDto<PageDto<CashRegisterDto>> getAllCashRegisters(int page, int size, String sortDirection,
            String sortBy);

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
    ApiSuccessDto<CashRegisterEnum> getCashRegisterStatus();

    ApiSuccessDto<CashRegisterDto> getCurrentOpenedCashRegister();

    ApiSuccessDto<List<CashRegisterDto>> getAvailableCashRegistersToOpen();

    CashRegisterEntity validateCashRegister();

}