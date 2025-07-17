package com.example.demo.controller.restaurantTable;

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
import com.example.demo.dto.restaurantTable.CreateRestaurantTableDto;
import com.example.demo.dto.restaurantTable.RestaurantTableDto;
import com.example.demo.dto.restaurantTable.UpdateRestaurantTableDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Restaurant Tables", description = "Operations related to restaurant tables")
public interface IRestaurantTableController {

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get all restaurant tables", description = "Get all restaurant tables")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping
        @Parameter(name = "page", description = "Page number", example = "1")
        @Parameter(name = "size", description = "Page size", example = "10")
        @Parameter(name = "number", description = "Restaurant table number", example = "1")
        @Parameter(name = "status", description = "Restaurant table status", example = "AVAILABLE")
        @Parameter(name = "section", description = "Restaurant table section", example = "SECTION 1")
        @Parameter(name = "sortBy", description = "Sort by field", example = "id")
        @Parameter(name = "sortDirection", description = "Sort direction", example = "asc")
        ResponseEntity<ApiSuccessDto<PageDto<RestaurantTableDto>>> getAllRestaurantTables(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "") String number,
                        @RequestParam(defaultValue = "") String status,
                        @RequestParam(defaultValue = "") String section,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDirection);

        @PostMapping
        @AcceptLanguageHeader
        @Operation(summary = "Create restaurant table", description = "Creates a new restaurant table")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Restaurant table created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<Void>> createRestaurantTable(
                        @Valid @RequestBody CreateRestaurantTableDto restaurantTableEntity);

        @GetMapping("/{id}")
        @AcceptLanguageHeader
        @Operation(summary = "Get restaurant table by ID", description = "Gets a restaurant table by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Restaurant table retrieved successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Restaurant table not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<RestaurantTableDto>> getRestaurantTable(@PathVariable(required = true) Integer id);

        @PutMapping("/{id}")
        @AcceptLanguageHeader
        @Operation(summary = "Update restaurant table", description = "Updates a restaurant table by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Restaurant table updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "404", description = "Restaurant table not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<Void>> updateRestaurantTable(@PathVariable(required = true) Integer id,
                        @Valid @RequestBody UpdateRestaurantTableDto restaurantTableEntity);
}
