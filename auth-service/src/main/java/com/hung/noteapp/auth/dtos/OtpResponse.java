package com.hung.noteapp.auth.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OtpResponse {
    private Long userId;
    private String message;
    private String email;
    private LocalDateTime CreatedAt;
    private LocalDateTime ExpiresAt;
}