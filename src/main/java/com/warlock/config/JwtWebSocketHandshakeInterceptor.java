package com.warlock.config;

import com.warlock.service.JwtService;
import com.warlock.service.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class JwtWebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    private final UserService userService;

    public JwtWebSocketHandshakeInterceptor(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            log.info("[WebSocket] Attempt to initialize WebSocket connection");
            String token = servletRequest.getServletRequest().getParameter("token");

            if (StringUtils.isNotEmpty(token)) {
                log.info("[WebSocket] token={}", token);
                String login = jwtService.extractUsername(token);
                UserDetails userDetails = userService
                        .userDetailsService()
                        .loadUserByUsername(login);

                var user = userService.getByLogin(login);
                if (jwtService.isTokenValid(token, userDetails)) {
                    attributes.put("userId", user.getId());
                    log.info("[WebSocket] User {} is successfully initialized WebSocket connection", user);
                    return true;
                } else {
                    log.error("[WebSocket] token is not valid");
                }
            }
        }
        log.error("[WebSocket] Attempt to initialize WebSocket connection failed");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Постобработка после установки соединения
    }
}