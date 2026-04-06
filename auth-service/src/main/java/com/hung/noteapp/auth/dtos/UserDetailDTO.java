package com.hung.noteapp.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailDTO {
    private String accessToken;
    private String tokenType;
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDateTime lastLoginAt;
    private String phone;
    private String avatarUrl;
    private Long roleId;
}