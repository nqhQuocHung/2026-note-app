package com.hung.noteapp.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDTO {
    private Long userId;
    private String otp;
    private String newPassword;
}
