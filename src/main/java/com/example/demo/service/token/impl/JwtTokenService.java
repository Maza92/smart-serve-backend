package com.example.demo.service.token.impl;

import org.springframework.stereotype.Service;

import com.example.demo.service.token.IJwtTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements IJwtTokenService {

    private final JwtParser jwtParser;

    @Override
    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    @Override
    public String extractTokenHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
