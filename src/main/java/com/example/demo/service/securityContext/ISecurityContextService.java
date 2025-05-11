package com.example.demo.service.securityContext;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;

public interface ISecurityContextService {
    Authentication getAuthentication();

    Claims getClaims();

    String getSubject();

    String getEmail();

    boolean hasRole(String role);

    void setAuthentication(Authentication authentication);
}
