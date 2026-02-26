package com.hung.noteapp.auth.services;

import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.pojos.User;

public interface AuthService {
    User register(UserRegisterDTO dto);
}
