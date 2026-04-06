package com.hung.noteapp.auth.controllers;

import com.hung.noteapp.auth.dtos.AuthenticateDTO;
import com.hung.noteapp.auth.dtos.UserDetailDTO;
import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.pojos.User;
import com.hung.noteapp.auth.services.AuthService;
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


}