package com.hung.noteapp.auth.controllers;

import com.hung.noteapp.auth.dtos.ChangePasswordOtpRequest;
import com.hung.noteapp.auth.dtos.OtpResponse;
import com.hung.noteapp.auth.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/otps")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/change-password")
    public ResponseEntity<?> createChangePasswordOtp(@RequestBody ChangePasswordOtpRequest request) {
        try {
            OtpResponse response = otpService.createChangePasswordOtp(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}