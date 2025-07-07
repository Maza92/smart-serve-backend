package com.example.demo.controller.inventory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiErrorDto;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.dashboard.InventoryDashboardDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Inventory", description = "Inventory API")
public interface IInventoryDashboardController {

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get dashboard", description = "Get the inventory dashboard")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @GetMapping("/dashboard")
        ResponseEntity<ApiSuccessDto<InventoryDashboardDto>> getDashboard();

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Refresh dashboard", description = "Refresh the inventory dashboard")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class)))
        })
        @PostMapping("/refresh")
        ResponseEntity<Void> refreshDashboard();
}
