package com.example.demo.controller.cashMovement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.cashMovement.CashMovementDto;
import com.example.demo.dto.cashMovement.CreateCashMovementDto;
import com.example.demo.enums.CashMovementTypeEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cash Movement", description = "Cash Movement API")
public interface ICashMovementController {

    @PostMapping
    @AcceptLanguageHeader
    @Operation(summary = "Create cash movement", description = "Creates a new cash movement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cash movement created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
    })
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<CashMovementDto>> createCashMovement(
            @Valid @RequestBody CreateCashMovementDto createCashMovementDto);

    @GetMapping
    @AcceptLanguageHeader
    @Operation(summary = "Get cash movements", description = "Gets a paginated list of cash movements with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cash movements retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
    })
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<PageDto<CashMovementDto>>> getCashMovements(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer cashRegisterId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) CashMovementTypeEnum movementType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection);

    @GetMapping("/{id}")
    @AcceptLanguageHeader
    @Operation(summary = "Get cash movement by ID", description = "Gets a cash movement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cash movement retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Cash movement not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
    })
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<CashMovementDto>> getCashMovementById(@PathVariable Integer id);

    @DeleteMapping("/{id}")
    @AcceptLanguageHeader
    @Operation(summary = "Delete cash movement", description = "Soft deletes a cash movement by setting active to false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cash movement deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Cash movement not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
    })
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<Void>> deleteCashMovement(@PathVariable Integer id);
}