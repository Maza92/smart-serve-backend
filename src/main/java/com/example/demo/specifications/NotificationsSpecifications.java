package com.example.demo.specifications;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.NotificationTypeEnum;

public class NotificationsSpecifications {

    public static Specification<NotificationsEntity> userEquals(UserEntity user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<NotificationsEntity> isReadEquals(Boolean isRead) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isRead"), isRead);
    }

    public static Specification<NotificationsEntity> typeEquals(NotificationTypeEnum type) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<NotificationsEntity> dateRangeBetween(Instant startInstant, Instant endInstant) {
        return (root, query, criteriaBuilder) -> {
            if (startInstant != null && endInstant != null) {
                return criteriaBuilder.between(root.get("createdAt"), startInstant, endInstant);
            } else if (startInstant != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startInstant);
            } else if (endInstant != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endInstant);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
}