package com.hung.noteapp.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordOtpRequest {
    private Long userId;
    private String oldPassword;
}
