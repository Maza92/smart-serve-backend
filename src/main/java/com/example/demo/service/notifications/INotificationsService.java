package com.example.demo.service.notifications;

import java.util.List;

import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.UserEntity;

public interface INotificationsService {
    void createOrderReadyNotification(OrderEntity order);

    void createGenericNotification(UserEntity user, String message, String type, String relatedEntityType,
            Integer relatedEntityId);

    NotificationsEntity markAsRead(Integer notificationId, UserEntity user);

    List<NotificationsEntity> getUnreadNotificationsForUser(UserEntity user);

}
