package com.example.demo.dto.api;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApiSuccessDto<T> {
    private int status;
    private String message;
    private Instant timestamp;
    private String path;
    private String method;
    private T data;

    public ApiSuccessDto(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public ApiSuccessDto(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now();
        this.data = data;
    }

    public ApiSuccessDto(int status, String message, Instant timestamp, T data) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }

    public static <T> ApiSuccessDto<T> of(int status, String message) {
        return new ApiSuccessDto<>(status, message, Instant.now(), null);
    }

    public static <T> ApiSuccessDto<T> of(int status, String message, T data) {
        return new ApiSuccessDto<>(status, message, Instant.now(), null, null, data);
    }
}
