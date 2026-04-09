package com.hung.noteapp.auth.controllers;

import com.hung.noteapp.auth.dtos.*;
import com.hung.noteapp.auth.dtos.requests.RefreshTokenRequest;
import com.hung.noteapp.auth.dtos.responses.TokenResponseDTO;
import com.hung.noteapp.auth.services.AuthService;
import com.hung.noteapp.auth.services.OtpService;
import com.hung.noteapp.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping(
            value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDTO> register(
            @ModelAttribute UserRegisterDTO dto
    ) {
        UserResponseDTO response = authService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDetailDTO> login(@RequestBody AuthenticateDTO request) {
        UserDetailDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePasswordWithOtp(@RequestBody ChangePasswordDTO request) {
        try {
            String message = authService.changePasswordWithOtp(request);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordWithOtp(@RequestBody ChangePasswordDTO request) {
        try {
            String message = authService.forgotPasswordWithOtp(request);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping(
            value = "/user/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @ModelAttribute UserUpdateDTO dto
    ) {
        UserResponseDTO response = userService.updateUser(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/user/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> patchUser(
            @PathVariable Long id,
            @ModelAttribute UserUpdateDTO dto
    ) {
        try {
            UserResponseDTO response = userService.update(id, dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/otp-change-password")
    public ResponseEntity<?> createChangePasswordOtp(@RequestBody ChangePasswordOtpDTO request) {
        try {
            OtpResponse response = otpService.createChangePasswordOtp(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            TokenResponseDTO response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/otp-forgot-password")
    public ResponseEntity<?> createForgotPasswordOtp(@RequestBody ForgotPasswordDTO request) {
        try {
            OtpResponse response = otpService.createForgotPasswordOtp(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}