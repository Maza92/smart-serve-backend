package com.example.demo.controller.cashRegister;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.controller.docBase.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.cashRegister.ClosedCashRegisterDto;
import com.example.demo.dto.cashRegister.OpenCashRegisterDto;
import com.example.demo.dto.cashRegister.PartialCreateCashRegisterDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
}
