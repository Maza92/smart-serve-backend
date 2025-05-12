package com.example.demo.service.securityContext.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.security.object.JwtAuthenticationToken;
import com.example.demo.service.securityContext.ISecurityContextService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityContextService implements ISecurityContextService {

    private final ApiExceptionFactory apiExceptionFactory;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Claims getClaims() {
        Authentication auth = getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getClaims();
        }
        throw apiExceptionFactory.authException("auth.token.invalid");
    }

    public String getSubject() {
        return getClaims().getSubject();
    }

    public Integer getSubjectAsInt() {
        String subject = getSubject();
        if (subject == null) {
            throw apiExceptionFactory.authException("auth.token.invalid");
        }
        try {
            return Integer.parseInt(subject);
        } catch (NumberFormatException e) {
            throw apiExceptionFactory.authException("auth.token.invalid");
        }
    }

    public String getEmail() {
        return getClaims().get("email", String.class);
    }

    public boolean hasRole(String role) {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    public void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
