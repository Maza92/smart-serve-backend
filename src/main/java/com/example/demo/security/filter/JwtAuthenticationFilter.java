package com.example.demo.security.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.object.JwtAuthenticationToken;
import com.example.demo.service.token.IJwtTokenService;
import com.example.demo.service.token.validator.ITokenValidationService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final IJwtTokenService jwtTokenService;
	private final ITokenValidationService tokenValidationService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String token = this.jwtTokenService.extractTokenHeader(request);

		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Claims claims = tokenValidationService.validateToken(token);

			JwtAuthenticationToken authentication = new JwtAuthenticationToken(token, claims);
			authentication.setAuthenticated(true);

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}
