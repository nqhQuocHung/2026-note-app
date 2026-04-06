package com.hung.noteapp.auth.services;

import com.hung.noteapp.auth.dtos.AuthenticateDTO;
import com.hung.noteapp.auth.dtos.UserDetailDTO;
import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.pojos.User;

public interface AuthService {
    UserResponseDTO register(UserRegisterDTO dto);
    UserDetailDTO login(AuthenticateDTO request);
}
