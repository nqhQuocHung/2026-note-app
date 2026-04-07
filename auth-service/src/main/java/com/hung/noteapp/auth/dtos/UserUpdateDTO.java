package com.hung.noteapp.auth.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    private Integer gender;
    private String email;
    private String phone;
    private MultipartFile avatar;
}