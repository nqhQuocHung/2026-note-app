package com.hung.noteapp.auth.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(String username);
    String generateRefreshToken(String username);
    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenValid(String token, String username);
}