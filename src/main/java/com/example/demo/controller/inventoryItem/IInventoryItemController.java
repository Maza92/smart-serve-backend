package com.example.demo.controller.inventoryItem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.inventoryItem.CreateInventoryItemDto;
import com.example.demo.dto.inventoryItem.InventoryItemDto;
import com.example.demo.dto.inventoryItem.UpdateInventoryItemDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Inventory Item", description = "Inventory Item API")
public interface IInventoryItemController {

        @GetMapping
        @AcceptLanguageHeader
        @Operation(summary = "Get inventory items", description = "Gets a paginated list of inventory items with optional filters")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))) })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<PageDto<InventoryItemDto>>> getInventoryItems(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String location,
                        @RequestParam(required = false) String isActive,
                        @RequestParam(defaultValue = "name") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDirection);

        @GetMapping("/supplier/{supplierId}")
        @AcceptLanguageHeader
        @Operation(summary = "Get inventory items by supplier", description = "Gets a paginated list of inventory items by supplier with optional filters")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))) })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<PageDto<InventoryItemDto>>> getInventoryItemsBySupplier(
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "1") int page,
                        @PathVariable(required = true) int supplierId);

        @GetMapping("/{id}")
        @AcceptLanguageHeader
        @Operation(summary = "Get inventory item by ID", description = "Gets an inventory item by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory item retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Inventory item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<InventoryItemDto>> getInventoryItemById(@PathVariable(required = true) int id);

        @PostMapping
        @AcceptLanguageHeader
        @Operation(summary = "Create inventory item", description = "Creates a new inventory item")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Inventory item created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<InventoryItemDto>> createInventoryItem(
                        @Valid @RequestBody CreateInventoryItemDto createInventoryItemDto);

        @PutMapping("/{id}")
        @AcceptLanguageHeader
        @Operation(summary = "Update inventory item", description = "Updates an inventory item by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory item updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Inventory item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<InventoryItemDto>> updateInventoryItem(@PathVariable(required = true) int id,
                        @Valid @RequestBody UpdateInventoryItemDto updateInventoryItemDto);

        @DeleteMapping("/{id}")
        @AcceptLanguageHeader
        @Operation(summary = "Delete inventory item", description = "Deletes an inventory item by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory item deleted successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Inventory item not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<Void>> deleteInventoryItem(@PathVariable(required = true) int id);

}