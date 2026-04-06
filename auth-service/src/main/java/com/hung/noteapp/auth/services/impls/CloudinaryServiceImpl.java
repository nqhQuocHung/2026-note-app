package com.hung.noteapp.auth.services.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.hung.noteapp.auth.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);
            options.put("resource_type", "image");

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);
            return (String) result.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Upload avatar failed", e);
        }
    }

    @Override
    public void validateAvatarFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("avatar must be an image file");
        }

        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("avatar size must be less than 5MB");
        }
    }
}
