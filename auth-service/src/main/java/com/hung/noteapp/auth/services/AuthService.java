package com.hung.noteapp.auth.services;
import com.hung.noteapp.auth.dtos.*;
import com.hung.noteapp.auth.dtos.requests.RefreshTokenRequest;
import com.hung.noteapp.auth.dtos.responses.TokenResponseDTO;

public interface AuthService {
    UserResponseDTO register(UserRegisterDTO dto);
    UserDetailDTO login(AuthenticateDTO request);
    String changePasswordWithOtp(ChangePasswordDTO request);
    String forgotPasswordWithOtp(ChangePasswordDTO request);
    TokenResponseDTO refreshToken(RefreshTokenRequest request);}
