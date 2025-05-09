package com.example.demo.service.key;

import javax.crypto.SecretKey;

import jakarta.validation.constraints.NotNull;

public interface IJwtKeyService {
    @NotNull
    SecretKey generateKey();

    boolean isValidKey(byte @NotNull [] key);
}