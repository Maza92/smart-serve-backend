package com.example.demo.security.object;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.example.demo.entity.UserEntity;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final String token;
	private final Claims claims;
	private UserEntity user;

	public JwtAuthenticationToken(String token, Claims claims) {
		super(null);
		this.token = token;
		this.claims = claims;
		setAuthenticated(false);
	}

	public JwtAuthenticationToken(String token, Claims claims, UserEntity user,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.token = token;
		this.claims = claims;
		this.user = user;
		setAuthenticated(true);
	}

	public JwtAuthenticationToken(String token, Claims claims,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.token = token;
		this.claims = claims;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return user;
	}

	public Claims getClaims() {
		return claims;
	}

	@Override
	public String getName() {
		return claims.getSubject();
	}
}