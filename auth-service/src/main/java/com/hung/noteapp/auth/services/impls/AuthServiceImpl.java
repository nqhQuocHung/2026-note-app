package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.configurations.MailTemplateProperties;
import com.hung.noteapp.auth.dtos.AuthenticateDTO;
import com.hung.noteapp.auth.dtos.UserDetailDTO;
import com.hung.noteapp.auth.dtos.UserRegisterDTO;
import com.hung.noteapp.auth.dtos.UserResponseDTO;
import com.hung.noteapp.auth.enums.GenderEnum;
import com.hung.noteapp.auth.pojos.Role;
import com.hung.noteapp.auth.pojos.User;
import com.hung.noteapp.auth.repositories.RoleRepository;
import com.hung.noteapp.auth.repositories.UserRepository;
import com.hung.noteapp.auth.services.AuthService;
import com.hung.noteapp.auth.services.CloudinaryService;
import com.hung.noteapp.auth.services.EmailService;
import com.hung.noteapp.auth.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private  JwtService jwtService;

    @Autowired
    private MailTemplateProperties mailTemplateProperties;

    @Value("${app.avatar.default-url}")
    private String defaultAvatarUrl;

    @Value("${app.default-role}")
    private String defaultRole;

    @Value("${jwt.type}")
    private String jwtType;

    @Value("${app.avatar.max-size-bytes:2097152}")
    private long maxAvatarSizeBytes;

    @Override
    @Transactional
    public UserResponseDTO register(UserRegisterDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("password is required");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("email is required");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }

        Role roleUser = roleRepository.findByName(defaultRole)
                .orElseThrow(() -> new IllegalStateException("Role " + defaultRole + " not found"));

        String avatarUrl = defaultAvatarUrl;
        MultipartFile avatar = dto.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            cloudinaryService.validateAvatarFile(avatar);
            avatarUrl = cloudinaryService.uploadImage(avatar, "avatars");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .avatar(avatarUrl)
                .isActive(true)
                .roles(Set.of(roleUser))
                .build();

        User savedUser = userRepository.save(user);

        try {
            String subject = mailTemplateProperties.getSubject();

            String username = savedUser.getUsername();
            if (username == null || username.isBlank()) {
                username = savedUser.getFirstName() != null ? savedUser.getFirstName() : "";
            }

            String body = mailTemplateProperties.getBody()
                    .replace("{{username}}", username);

            emailService.sendHtmlEmail(
                    savedUser.getEmail(),
                    subject,
                    body
            );

        } catch (Exception e) {
            System.err.println("Send mail failed: " + e.getMessage());
        }

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .gender(GenderEnum.fromValue(savedUser.getGender()).name().toLowerCase())
                .phone(savedUser.getPhone())
                .avatarUrl(savedUser.getAvatar())
                .roleId(savedUser.getRoles() != null && !savedUser.getRoles().isEmpty()
                        ? savedUser.getRoles().iterator().next().getId()
                        : null)
                .build();
    }

    @Override
    @Transactional
    public UserDetailDTO login(AuthenticateDTO request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("password is required");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        user.setLastLoginAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateToken(savedUser.getUsername());

        return UserDetailDTO.builder()
                .accessToken(accessToken)
                .tokenType(jwtType)
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .phone(savedUser.getPhone())
                .avatarUrl(savedUser.getAvatar())
                .gender(GenderEnum.fromValue(savedUser.getGender()).name())
                .roleId(savedUser.getRoles() != null && !savedUser.getRoles().isEmpty()
                        ? savedUser.getRoles().iterator().next().getId()
                        : null)
                .lastLoginAt(savedUser.getLastLoginAt())
                .build();
    }

}
