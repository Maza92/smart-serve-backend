package com.example.demo.controller.supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.supplier.CreateSupplierDto;
import com.example.demo.dto.supplier.SupplierDto;
import com.example.demo.dto.supplier.UpdateSupplierDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Supplier", description = "Supplier management APIs")
public interface ISupplierController {

        @Operation(summary = "Get all suppliers", description = "Get all suppliers with pagination and sorting")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping
        ResponseEntity<ApiSuccessDto<PageDto<SupplierDto>>> getAllSuppliers(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String isActive,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "ASC") String sortDirection);

        @Operation(summary = "Get supplier by ID", description = "Get supplier by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping("/{id}")
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<SupplierDto>> getSupplierById(@PathVariable int id);

        @Operation(summary = "Create supplier", description = "Create a new supplier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Supplier created successfully", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping

        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<SupplierDto>> createSupplier(
                        @Valid @RequestBody CreateSupplierDto createSupplierDto);

        @Operation(summary = "Update supplier", description = "Update an existing supplier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Supplier updated successfully", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PutMapping("/{id}")
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<SupplierDto>> updateSupplier(@PathVariable int id,
                        @Valid @RequestBody UpdateSupplierDto updateSupplierDto);

        @Operation(summary = "Delete supplier", description = "Delete an existing supplier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Supplier deleted successfully", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @DeleteMapping("/{id}")
        @SecurityRequirement(name = "Auth")
        ResponseEntity<ApiSuccessDto<Void>> deleteSupplier(@PathVariable int id);
}