package com.warlock.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Component
public class AuthWebSocketHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            @NonNull ServerHttpRequest request,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) {
        String username = (String) attributes.get("username");
        log.info("[AuthWebSocketHandler] Login {}", username);
        if (username != null) {
            log.info("[AuthWebSocketHandler] Principal is created");
            return () -> username;
        }
        log.error("[AuthWebSocketHandler] Principal is not created");
        return null;
    }
}