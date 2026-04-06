package com.hung.noteapp.auth.controllers;

import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @ModelAttribute UserRegisterDTO dto
    ) {
        UserResponseDTO response = userService.updateUser(id, dto);
        return ResponseEntity.ok(response);
    }
}