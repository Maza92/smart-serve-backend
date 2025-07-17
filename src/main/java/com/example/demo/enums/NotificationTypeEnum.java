package com.example.demo.enums;

public enum NotificationTypeEnum {
    INFO("info"),
    SUCCESS("success"),
    WARNING("warning"),
    ERROR("error");

    private final String value;

    NotificationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
