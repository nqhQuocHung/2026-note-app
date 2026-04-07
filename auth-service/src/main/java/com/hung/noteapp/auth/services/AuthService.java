package com.hung.noteapp.auth.services;
import com.hung.noteapp.auth.dtos.*;

public interface AuthService {
    UserResponseDTO register(UserRegisterDTO dto);
    UserDetailDTO login(AuthenticateDTO request);
    String changePasswordWithOtp(ChangePasswordDTO request);
}
