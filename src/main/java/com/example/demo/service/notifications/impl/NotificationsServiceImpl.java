package com.example.demo.service.notifications.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.notifications.NotificationDto;
import com.example.demo.dto.notifications.UpdateNotificationDto;
import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.INotificationMapper;
import com.example.demo.repository.NotificationsRepository;
import com.example.demo.service.notifications.INotificationsService;
import com.example.demo.service.securityContext.ISecurityContextService;
import com.example.demo.service.webSocket.IStompMessagingService;
import com.example.demo.specifications.NotificationsSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
                .type(NotificationTypeEnum.SUCCESS)
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
    public void createGenericNotification(UserEntity user, String message, NotificationTypeEnum type,
            String relatedEntityType,
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
    public NotificationsEntity markAsRead(Integer notificationId) {
        NotificationsEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("notification.not.found"));

        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationsEntity> getUnreadNotificationsForUser() {
        UserEntity currentUser = securityContextService.getUser();
        return notificationRepository.findTop20ByUserAndIsReadOrderByCreatedAtDesc(currentUser, false);
    }

    @Override
    public ApiSuccessDto<PageDto<NotificationDto>> getNotifications(int page, int size, Boolean isRead,
            NotificationTypeEnum type,
            String startDate, String endDate, String sortBy, String sortDirection) {

        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

        UserEntity currentUser = securityContextService.getUser();

        Specification<NotificationsEntity> spec = Specification
                .where(NotificationsSpecifications.userEquals(currentUser));

        if (isRead != null) {
            spec = spec.and(NotificationsSpecifications.isReadEquals(isRead));
        }

        if (type != null) {
            spec = spec.and(NotificationsSpecifications.typeEquals(type));
        }

        Instant startInstant = null;
        Instant endInstant = null;

        if (startDate != null && !startDate.isBlank()) {
            startInstant = Instant.parse(startDate);
        }

        if (endDate != null && !endDate.isBlank()) {
            endInstant = Instant.parse(endDate);
        }

        if (startInstant != null || endInstant != null) {
            spec = spec.and(NotificationsSpecifications.dateRangeBetween(startInstant, endInstant));
        }

        Page<NotificationsEntity> notifications = notificationRepository.findAll(spec, pageable);

        List<NotificationDto> notificationDtos = notifications.getContent().stream()
                .map(notification -> {
                    NotificationDto dto = notificationMapper.toDto(notification);
                    dto.setType(notification.getType().getValue());
                    return dto;
                })
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.notification.get.all.success"),
                PageDto.fromPage(notifications, notificationDtos));
    }

    @Override
    public ApiSuccessDto<NotificationDto> getNotificationById(Integer id) {
        UserEntity currentUser = securityContextService.getUser();

        NotificationsEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.notification.not.found"));

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw apiExceptionFactory.authException("exception.authorization.denied");
        }

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.notification.get.by.id.success"),
                notificationMapper.toDto(notification));
    }

    @Override
    @Transactional
    public ApiSuccessDto<NotificationDto> updateNotificationReadStatus(UpdateNotificationDto updateNotificationDto) {
        UserEntity currentUser = securityContextService.getUser();

        NotificationsEntity notification = notificationRepository.findByIdWithUser(updateNotificationDto.getId())
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.notification.not.found"));

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw apiExceptionFactory.authException("exception.authorization.denied");
        }

        notification.setRead(updateNotificationDto.getIsRead());
        NotificationsEntity updatedNotification = notificationRepository.save(notification);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.notification.updated"),
                notificationMapper.toDto(updatedNotification));
    }
}
