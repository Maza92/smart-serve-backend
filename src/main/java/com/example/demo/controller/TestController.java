package com.example.demo.controller;

import java.util.List;

import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final SimpUserRegistry simpUserRegistry;

    @GetMapping("/hello")
    @Parameter(name = "Accept-Language", in = ParameterIn.HEADER, required = true, description = "Language code (e.g., 'en' for English, 'es' for Spanish)", example = "en", schema = @Schema(type = "string", defaultValue = "en"))
    @SecurityRequirement(name = "Auth")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/greet")
    @Parameter(name = "Accept-Language", in = ParameterIn.HEADER, required = true, description = "Language code (e.g., 'en' for English, 'es' for Spanish)", example = "en", schema = @Schema(type = "string", defaultValue = "en"))
    @SecurityRequirement(name = "Auth")
    public String greet() {
        return "Greetings from the TestController!";
    }

    @GetMapping("/status")
    @Parameter(name = "Accept-Language", in = ParameterIn.HEADER, required = true, description = "Language code (e.g., 'en' for English, 'es' for Spanish)", example = "en", schema = @Schema(type = "string", defaultValue = "en"))
    @SecurityRequirement(name = "Auth")
    public String status() {
        return "TestController is up and running!";
    }

    @GetMapping("/ws-users")
    public List<String> connectedUsers() {
        return simpUserRegistry.getUsers()
                .stream()
                .map(SimpUser::getName)
                .toList();
    }
}
