package com.example.demo.controller.cashRegister;

import java.io.InputStream;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashRegister.CashRegisterDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;
import com.example.demo.dto.data.ImportResultDto;
import com.example.demo.enums.CashRegisterEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cash", description = "Cash Operations API")
public interface ICashRegisterController {

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @PostMapping("/create")
        @Operation(summary = "Create cash register", description = "Creates a new cash register with minimal information")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Cash register created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        ResponseEntity<ApiSuccessDto<Void>> createCashRegister(
                        @Parameter(description = "Data to create cash register") @Valid @RequestBody PartialCreateCashRegisterDto cashRegisterCreateDto);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @PutMapping("/open/{cashRegisterId}")
        @Operation(summary = "Open cash register", description = "Opens an existing cash register with initial amount")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cash register opened successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request or cash register already closed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Cash register not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        ResponseEntity<ApiSuccessDto<Void>> openCashRegister(
                        @Parameter(description = "Data to open cash register") @Valid @RequestBody OpenCashRegisterDto cashRegisterCreateDto,
                        @Parameter(description = "Cash register ID", required = true) @PathVariable Integer cashRegisterId);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @PutMapping("/close/{cashRegisterId}")
        @Operation(summary = "Close cash register", description = "Closes an existing cash register with final amount")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cash register closed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request or cash register already closed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Cash register not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        ResponseEntity<ApiSuccessDto<Void>> closeCashRegister(
                        @Parameter(description = "Data to close cash register") @Valid @RequestBody ClosedCashRegisterDto cashRegisterCreateDto,
                        @Parameter(description = "Cash register ID", required = true) @PathVariable Integer cashRegisterId);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get all cash registers", description = "Retrieves a paginated list of all cash registers")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cash registers retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping
        @Parameter(name = "page", description = "Page number", example = "1")
        @Parameter(name = "size", description = "Page size", example = "10")
        ResponseEntity<ApiSuccessDto<PageDto<CashRegisterDto>>> getAllCashRegisters(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                        @RequestParam(required = false, defaultValue = "id") String sortBy);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get cash register status", description = "Retrieves the status of the cash register")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cash register status retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping("/status")
        ResponseEntity<ApiSuccessDto<CashRegisterEnum>> getCashRegisterStatus();

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get current opened cash register", description = "Retrieves the currently opened cash register")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Current opened cash register retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "No opened cash register found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping("/current")
        ResponseEntity<ApiSuccessDto<CashRegisterDto>> getCurrentOpenedCashRegister();

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get available cash registers to open", description = "Retrieves a list of cash registers that can be opened")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Available cash registers retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping("/available")
        ResponseEntity<ApiSuccessDto<List<CashRegisterDto>>> getAvailableCashRegistersToOpen();

}
