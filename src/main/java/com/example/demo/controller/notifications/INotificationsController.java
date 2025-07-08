package com.example.demo.controller.notifications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.annotation.AcceptLanguageHeader;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.notifications.NotificationDto;
import com.example.demo.dto.notifications.UpdateNotificationDto;
import com.example.demo.enums.NotificationTypeEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notifications", description = "Notifications Operations API")
public interface INotificationsController {

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get notifications", description = "Get all notifications with pagination and filtering")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)))
        })
        @GetMapping
        @Parameter(name = "page", description = "Page number", required = false, in = ParameterIn.QUERY)
        @Parameter(name = "size", description = "Page size", required = false, in = ParameterIn.QUERY)
        @Parameter(name = "isRead", description = "Filter by read status", required = false, in = ParameterIn.QUERY)
        @Parameter(name = "type", description = "Filter by notification type", required = false, in = ParameterIn.QUERY)
        @Parameter(name = "startDate", description = "Filter by start date", required = false, in = ParameterIn.QUERY)
        @Parameter(name = "endDate", description = "Filter by end date", required = false, in = ParameterIn.QUERY)
        @Parameter(name = "sortBy", description = "Sort field", required = false, in = ParameterIn.QUERY)
        @Parameter(name = "sortDirection", description = "Sort direction", required = false, in = ParameterIn.QUERY)
        ResponseEntity<ApiSuccessDto<PageDto<NotificationDto>>> getNotifications(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) Boolean isRead,
                        @RequestParam(required = false) NotificationTypeEnum type,
                        @RequestParam(required = false) String startDate,
                        @RequestParam(required = false) String endDate,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "DESC") String sortDirection);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Get notification by ID", description = "Get a specific notification by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)))
        })
        @GetMapping("/{id}")
        ResponseEntity<ApiSuccessDto<NotificationDto>> getNotificationById(@PathVariable Integer id);

        @AcceptLanguageHeader
        @SecurityRequirement(name = "Auth")
        @Operation(summary = "Update notification read status", description = "Update the read status of a notification")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessDto.class)))
        })
        @PutMapping
        ResponseEntity<ApiSuccessDto<NotificationDto>> updateNotificationReadStatus(
                        @RequestBody UpdateNotificationDto updateNotificationDto);
}