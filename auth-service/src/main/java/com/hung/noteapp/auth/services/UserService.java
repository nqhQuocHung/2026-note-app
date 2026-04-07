package com.hung.noteapp.auth.services;

import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.dtos.UserUpdateDTO;

public interface UserService {
    UserResponseDTO updateUser(Long id, UserUpdateDTO dto);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO update(Long id, UserUpdateDTO dto);
}
