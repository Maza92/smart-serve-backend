package com.example.demo.service.token.validator.impl;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.repository.JwtRepository;
import com.example.demo.service.token.validator.ITokenValidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenValidationService implements ITokenValidationService {
	private final JwtRepository jwtRepository;
	private final ApiExceptionFactory apiExceptionFactory;
	private final JwtParser jwtParser;

	public Claims validateToken(String token) {
		Claims claims = extractClaims(token);

		if (!jwtRepository.existsByToken(token)) {
			throw apiExceptionFactory.authException("auth.token.not.exists");
		}

		if (jwtRepository.isRevoked(token)) {
			throw apiExceptionFactory.authException("auth.token.revoked");
		}

		if (!jwtRepository.isValid(token)) {
			throw apiExceptionFactory.authException("auth.token.invalid");
		}

		if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
			throw apiExceptionFactory.authException("auth.token.expired");
		}

		return claims;
	}

	public Claims extractClaims(String token) {
		try {
			return jwtParser.parseSignedClaims(token).getPayload();
		} catch (ExpiredJwtException e) {
			throw apiExceptionFactory.authException("auth.token.expired");
		} catch (Exception e) {
			throw apiExceptionFactory.authException("auth.token.invalid");
		}
	}
}
