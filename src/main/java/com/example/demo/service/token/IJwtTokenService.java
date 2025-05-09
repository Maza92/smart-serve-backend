package com.example.demo.service.token;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface IJwtTokenService {
	boolean validateToken(String token);

	Claims getClaims(String token);

	String extractTokenHeader(HttpServletRequest request);
}
