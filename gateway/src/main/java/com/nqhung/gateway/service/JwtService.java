package com.nqhung.gateway.service;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface JwtService {
    String extractToken(ServerHttpRequest request);

    String extractUsername(String token);

    Long extractUserId(String token);

    boolean isTokenValid(String token);

    boolean isTokenExpired(String token);
}
