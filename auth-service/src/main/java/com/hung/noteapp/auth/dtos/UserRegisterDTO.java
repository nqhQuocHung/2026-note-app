package com.hung.noteapp.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Integer gender;
    private String email;
    private String phone;
    private MultipartFile avatar;
}