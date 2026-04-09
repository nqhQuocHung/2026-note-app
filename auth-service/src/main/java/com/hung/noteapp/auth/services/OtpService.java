package com.hung.noteapp.auth.services;

import com.hung.noteapp.auth.dtos.ChangePasswordOtpDTO;
import com.hung.noteapp.auth.dtos.ForgotPasswordDTO;
import com.hung.noteapp.auth.dtos.OtpResponse;

public interface OtpService {
    OtpResponse createChangePasswordOtp(ChangePasswordOtpDTO request);
    OtpResponse createForgotPasswordOtp(ForgotPasswordDTO request);
}
