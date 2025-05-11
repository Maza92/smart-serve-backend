package com.example.demo.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.object.JwtAuthenticationToken;
import com.example.demo.service.securityContext.ISecurityContextService;
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
	private final ISecurityContextService securityContextService;

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

			@SuppressWarnings("unchecked")
			Map<String, Object> roleMap = (Map<String, Object>) claims.get("role");
			String roleName = (String) roleMap.get("name");

			List<GrantedAuthority> authorities = List.of(
					new SimpleGrantedAuthority("ROLE_" + roleName));

			JwtAuthenticationToken authentication = new JwtAuthenticationToken(token, claims, authorities);
			authentication.setAuthenticated(true);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			securityContextService.setAuthentication(authentication);

		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}
