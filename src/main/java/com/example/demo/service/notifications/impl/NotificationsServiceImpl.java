package com.example.demo.service.notifications.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.INotificationMapper;
import com.example.demo.repository.NotificationsRepository;
import com.example.demo.service.notifications.INotificationsService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.service.webSocket.IStompMessagingService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationsServiceImpl implements INotificationsService {

    private final NotificationsRepository notificationRepository;
    private final ApiExceptionFactory apiExceptionFactory;
    private final ISecurityContextService securityContextService;
    private final IStompMessagingService stompMessagingService;
    private final INotificationMapper notificationMapper;
    private final MessageUtils messageUtils;

    @Override
    public void createOrderReadyNotification(OrderEntity order) {
        String message = "La orden de la Mesa " + order.getTable().getNumber() + " está lista para servir.";

        NotificationsEntity notification = NotificationsEntity.builder()
                .user(order.getUser())
                .message(message)
                .type("ORDER_READY")
                .isRead(false)
                .relatedEntityType("ORDER")
                .relatedEntityId(order.getId())
                .build();

        NotificationsEntity savedNotification = notificationRepository.save(notification);
        String userSubject = order.getUser().getId().toString();
        String destination = "/queue/notifications";
        stompMessagingService.sendToUser(userSubject, destination, notificationMapper.toDto(savedNotification));
    }

    @Override
    public void createGenericNotification(UserEntity user, String message, String type, String relatedEntityType,
            Integer relatedEntityId) {
        NotificationsEntity notification = NotificationsEntity.builder()
                .user(user)
                .message(message)
                .type(type)
                .isRead(false)
                .relatedEntityType(relatedEntityType)
                .relatedEntityId(relatedEntityId)
                .build();

        NotificationsEntity savedNotification = notificationRepository.save(notification);
        String userSubject = user.getId().toString();
        String destination = "/queue/notifications";
        stompMessagingService.sendToUser(userSubject, destination, notificationMapper.toDto(savedNotification));
    }

    @Override
    public NotificationsEntity markAsRead(Integer notificationId, UserEntity user) {
        NotificationsEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("notification.not.found"));

        notification.setRead(true);
        notification.setUser(user);

        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationsEntity> getUnreadNotificationsForUser(UserEntity user) {
        return notificationRepository.findTop20ByUserAndIsReadOrderByCreatedAtDesc(user, false);
    }
}
