package com.example.demo.controller.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.transaction.CreateOrderTransactionDto;
import com.example.demo.dto.transaction.CreateTransactionDto;
import com.example.demo.dto.transaction.TransactionDto;
import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Transaction", description = "Transaction management APIs")
public interface ITransactionController {

    @Operation(summary = "Create a new transaction", description = "Creates a new transaction with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<TransactionDto>> createTransaction(
            @Valid @RequestBody CreateTransactionDto createTransactionDto);

    @Operation(summary = "Create a new order transaction", description = "Creates a new transaction specifically for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order transaction created successfully", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/order")
    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<TransactionDto>> createOrderTransaction(
            @Valid @RequestBody CreateOrderTransactionDto createOrderTransactionDto);

    @Operation(summary = "Get all transactions", description = "Retrieves all transactions with pagination and filtering options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<PageDto<TransactionDto>>> getTransactions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer cashRegisterId,
            @RequestParam(required = false) Integer orderId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) PaymentMethodEnum paymentMethod,
            @RequestParam(required = false) TransactionTypeEnum transactionType,
            @RequestParam(required = false) TransactionStatusEnum status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection);

    @Operation(summary = "Get transaction by ID", description = "Retrieves a specific transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<TransactionDto>> getTransactionById(@PathVariable Integer id);

    @Operation(summary = "Delete transaction", description = "Soft deletes a transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted successfully", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    @AcceptLanguageHeader
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<Void>> deleteTransaction(@PathVariable Integer id);
}
