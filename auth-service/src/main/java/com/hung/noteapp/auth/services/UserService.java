package com.hung.noteapp.auth.services;

import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.dtos.UserResponseDTO;

public interface UserService {
    UserResponseDTO updateUser(Long id, UserRegisterDTO dto);
    UserResponseDTO getUserById(Long id);
}
