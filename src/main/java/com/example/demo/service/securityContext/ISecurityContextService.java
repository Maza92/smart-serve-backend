package com.example.demo.service.securityContext;

import org.springframework.security.core.Authentication;

import com.example.demo.entity.UserEntity;

import io.jsonwebtoken.Claims;

public interface ISecurityContextService {
    Authentication getAuthentication();

    Claims getClaims();

    String getSubject();

    Integer getSubjectAsInt();

    String getEmail();

    boolean hasRole(String role);

    void setAuthentication(Authentication authentication);

    UserEntity getUser();
}
