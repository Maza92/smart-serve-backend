package com.example.demo.service.parameter.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.entity.SystemParameterEntity;
import com.example.demo.repository.SystemParameterRepository;
import com.example.demo.service.parameter.IParameterService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParameterServiceImpl implements IParameterService {
    private final SystemParameterRepository systemParameterRepository;
    private final Map<String, String> parameterCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadParameters() {
        log.info("Loading system parameters into cache...");
        try {
            parameterCache.putAll(
                    systemParameterRepository.findAll().stream()
                            .collect(Collectors.toMap(
                                    SystemParameterEntity::getParamKey,
                                    SystemParameterEntity::getParamValue)));
            log.info("{} parameters loaded successfully.", parameterCache.size());
        } catch (Exception e) {
            log.error("Error loading system parameters.", e);
        }
    }

    public String getString(String key) {
        String value = parameterCache.get(key);
        if (value == null) {
            log.error("Parameter '{}' not found in cache.", key);
            throw new IllegalStateException("System parameter not configured: " + key);
        }
        return value;
    }

    public BigDecimal getBigDecimal(String key) {
        String value = getString(key);
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            log.error("Parameter '{}' value ('{}') is not a valid number.", key, value);
            throw new IllegalStateException("System parameter misconfigured: " + key);
        }
    }
}
