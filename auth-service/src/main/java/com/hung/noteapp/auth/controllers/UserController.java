package com.hung.noteapp.auth.controllers;

import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.dtos.UserUpdateDTO;
import com.hung.noteapp.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
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
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @ModelAttribute UserUpdateDTO dto
    ) {
        UserResponseDTO response = userService.updateUser(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}