package com.hung.noteapp.auth.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadImage(MultipartFile file, String folder);
    void validateAvatarFile(MultipartFile file);
}
