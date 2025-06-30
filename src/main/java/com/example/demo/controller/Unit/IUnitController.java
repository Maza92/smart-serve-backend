package com.example.demo.controller.Unit;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.unit.UnitDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Unit", description = "Operations related to units")
public interface IUnitController {

    @AcceptLanguageHeader
    @Operation(summary = "Get all units", description = "Get all units")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ApiSuccessDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    @SecurityRequirement(name = "Auth")
    ResponseEntity<ApiSuccessDto<List<UnitDto>>> getUnits();

}
