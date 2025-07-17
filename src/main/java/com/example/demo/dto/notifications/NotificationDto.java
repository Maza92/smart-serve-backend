package com.example.demo.dto.notifications;

import com.example.demo.enums.NotificationTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class NotificationDto {
    Integer id;
    String message;
    String type;
    boolean isRead;
    String relatedEntityType;
    Integer relatedEntityId;
    Integer userId;
    String userName;
}
