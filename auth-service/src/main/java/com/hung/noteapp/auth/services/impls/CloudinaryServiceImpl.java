package com.hung.noteapp.auth.services.impls;

import com.cloudinary.Cloudinary;
import com.hung.noteapp.auth.services.CloudinaryService;
import com.hung.noteapp.auth.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    private final MessageService messageService;

    @Value("${app.avatar.max-size-bytes:5242880}")
    private long maxAvatarSizeBytes;

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);
            options.put("resource_type", "image");

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);
            return (String) result.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException(messageService.get("cloudinary.upload_failed"), e);
        }
    }

    @Override
    public void validateAvatarFile(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException(messageService.get("avatar.required"));
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException(messageService.get("validation.file_empty"));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(messageService.get("avatar.invalid_type"));
        }

        if (file.getSize() > maxAvatarSizeBytes) {
            throw new IllegalArgumentException(messageService.get("avatar.invalid_size"));
        }
    }
}