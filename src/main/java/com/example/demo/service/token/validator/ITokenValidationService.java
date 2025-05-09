package com.example.demo.service.token.validator;

import io.jsonwebtoken.Claims;

public interface ITokenValidationService {
	Claims validateToken(String token);

	Claims extractClaims(String token);
}
