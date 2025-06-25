package com.example.demo.controller.inventory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.UpdateInventoryItemStockDto;
import com.example.demo.dto.inventory.UpdateInventoryItemsStockBatchDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Inventory", description = "Inventory API")
public interface IInventoryController {

        @PostMapping("/update-stock")
        @AcceptLanguageHeader
        @Operation(summary = "Update inventory stock", description = "Updates the stock of an inventory item")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Stock updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Inventory item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<Void>> updateStock(@Valid @RequestBody UpdateInventoryItemStockDto request);

        @PostMapping("/update-stocks-batch")
        @AcceptLanguageHeader
        @Operation(summary = "Update inventory stocks in batch", description = "Updates the stock of multiple inventory items")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Stocks updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Inventory item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<Void>> updateStocksBatch(
                        @Valid @RequestBody UpdateInventoryItemsStockBatchDto request);

        @PostMapping("/update-stocks-batch-async")
        @AcceptLanguageHeader
        @Operation(summary = "Update inventory stocks in batch asynchronously", description = "Updates the stock of multiple inventory items asynchronously")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Stocks updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Inventory item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        CompletableFuture<ResponseEntity<ApiSuccessDto<Void>>> updateStocksBatchAsync(
                        @Valid @RequestBody UpdateInventoryItemsStockBatchDto request);

}