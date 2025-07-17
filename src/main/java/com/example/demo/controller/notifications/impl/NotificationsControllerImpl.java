package com.example.demo.controller.notifications.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.notifications.INotificationsController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.notifications.NotificationDto;
import com.example.demo.dto.notifications.UpdateNotificationDto;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.service.notifications.INotificationsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class NotificationsControllerImpl implements INotificationsController {

    private final INotificationsService notificationsService;

    @Override
    public ResponseEntity<ApiSuccessDto<PageDto<NotificationDto>>> getNotifications(int page, int size, Boolean isRead,
            NotificationTypeEnum type, String startDate, String endDate, String sortBy, String sortDirection) {
        return ResponseEntity.ok(notificationsService.getNotifications(page, size, isRead, type, startDate, endDate,
                sortBy, sortDirection));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<NotificationDto>> getNotificationById(Integer id) {
        return ResponseEntity.ok(notificationsService.getNotificationById(id));
    }

    @Override
    public ResponseEntity<ApiSuccessDto<NotificationDto>> updateNotificationReadStatus(
            UpdateNotificationDto updateNotificationDto) {
        return ResponseEntity.ok(notificationsService.updateNotificationReadStatus(updateNotificationDto));
    }
}