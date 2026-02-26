package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.pojos.Role;
import com.hung.noteapp.auth.pojos.User;
import com.hung.noteapp.auth.repositories.RoleRepository;
import com.hung.noteapp.auth.repositories.UserRepository;
import com.hung.noteapp.auth.services.AuthService;
import com.hung.noteapp.auth.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Value("${app.avatar.default-url}")
    private String defaultAvatarUrl;

    @Value("${app.avatar.max-size-bytes:2097152}")
    private long maxAvatarSizeBytes;

    @Override
    @Transactional
    public User register(UserRegisterDTO dto) {
        // Basic validate
        if (dto.getUsername() == null || dto.getUsername().isBlank())
            throw new IllegalArgumentException("username is required");
        if (dto.getPassword() == null || dto.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");
        if (dto.getEmail() == null || dto.getEmail().isBlank())
            throw new IllegalArgumentException("email is required");

        // Unique checks
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("username already exists");
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("email already exists");

        // Default role USER
        Role roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        // Avatar logic
        String avatarUrl = defaultAvatarUrl;
        MultipartFile avatar = dto.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            validateAvatarFile(avatar);
            avatarUrl = cloudinaryService.uploadImage(avatar, "avatars");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .avatar(avatarUrl)
                .isActive(true)
                .roles(Set.of(roleUser))
                .build();

        return userRepository.save(user);
    }

    private void validateAvatarFile(MultipartFile file) {
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/"))
            throw new IllegalArgumentException("avatar must be an image");

        if (file.getSize() > maxAvatarSizeBytes)
            throw new IllegalArgumentException("avatar max size is " + maxAvatarSizeBytes + " bytes");
    }
}
