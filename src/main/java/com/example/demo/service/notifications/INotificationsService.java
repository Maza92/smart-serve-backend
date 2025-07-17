package com.example.demo.service.notifications;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.notifications.NotificationDto;
import com.example.demo.dto.notifications.UpdateNotificationDto;
import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.NotificationTypeEnum;

public interface INotificationsService {
        void createOrderReadyNotification(OrderEntity order);

        void createGenericNotification(UserEntity user, String message, NotificationTypeEnum type,
                        String relatedEntityType,
                        Integer relatedEntityId);

        NotificationsEntity markAsRead(Integer notificationId);

        List<NotificationsEntity> getUnreadNotificationsForUser();

        ApiSuccessDto<PageDto<NotificationDto>> getNotifications(int page, int size, Boolean isRead,
                        NotificationTypeEnum type,
                        String startDate, String endDate, String sortBy, String sortDirection);

        ApiSuccessDto<NotificationDto> getNotificationById(Integer id);

        ApiSuccessDto<NotificationDto> updateNotificationReadStatus(UpdateNotificationDto updateNotificationDto);
}
