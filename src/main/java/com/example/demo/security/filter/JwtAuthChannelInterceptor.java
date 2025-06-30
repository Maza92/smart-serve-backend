package com.example.demo.security.filter;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo.security.object.JwtAuthenticationToken;
import com.example.demo.service.token.validator.ITokenValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

    private final ITokenValidationService tokenValidationService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.debug("Processing WebSocket CONNECT command");

            final String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                log.debug("JWT token found in WebSocket connection");

                try {
                    Claims claims = tokenValidationService.validateToken(token);
                    log.debug("JWT token validated successfully for user: {}", claims.getSubject());

                    @SuppressWarnings("unchecked")
                    Map<String, Object> roleMap = (Map<String, Object>) claims.get("role");
                    String roleName = (String) roleMap.get("name");
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(token, claims, authorities);
                    accessor.setUser(authentication);

                    log.info("WebSocket authentication successful for user: {} with role: {}", claims.getSubject(),
                            roleName);

                } catch (Exception e) {
                    log.warn("WebSocket JWT authentication failed: {}", e.getMessage());
                }
            } else {
                log.debug("No Authorization header found in WebSocket connection");
            }
        }
        return message;
    }
}
