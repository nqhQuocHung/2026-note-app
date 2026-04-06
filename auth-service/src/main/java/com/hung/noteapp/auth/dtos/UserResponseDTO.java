package com.hung.noteapp.auth.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String avatarUrl;
    private Long roleId;
}
