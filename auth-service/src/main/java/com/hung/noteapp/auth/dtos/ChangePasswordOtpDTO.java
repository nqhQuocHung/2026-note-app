package com.hung.noteapp.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordOtpDTO {
    private Long userId;
    private String oldPassword;
}
