package com.hung.noteapp.auth.services;

import com.hung.noteapp.auth.dtos.ChangePasswordOtpRequest;
import com.hung.noteapp.auth.dtos.OtpResponse;

public interface OtpService {
    OtpResponse createChangePasswordOtp(ChangePasswordOtpRequest request);
}
