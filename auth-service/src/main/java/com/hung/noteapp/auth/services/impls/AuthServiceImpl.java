package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.configurations.MailTemplateProperties;
import com.hung.noteapp.auth.dtos.*;
import com.hung.noteapp.auth.enums.GenderEnum;
import com.hung.noteapp.auth.enums.OtpPurpose;
import com.hung.noteapp.auth.pojos.Role;
import com.hung.noteapp.auth.pojos.User;
import com.hung.noteapp.auth.pojos.UserOtp;
import com.hung.noteapp.auth.repositories.RoleRepository;
import com.hung.noteapp.auth.repositories.UserOtpRepository;
import com.hung.noteapp.auth.repositories.UserRepository;
import com.hung.noteapp.auth.services.*;
import lombok.RequiredArgsConstructor;
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

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final JwtService jwtService;
    private final MailTemplateProperties mailTemplateProperties;
    private final MessageService messageService;
    private final UserOtpRepository userOtpRepository;

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
            throw new IllegalArgumentException(messageService.get("auth.username_required"));
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException(messageService.get("auth.password_required"));
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException(messageService.get("auth.email_required"));
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException(messageService.get("user.username_exists"));
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(messageService.get("user.email_exists"));
        }

        Role roleUser = roleRepository.findByName(defaultRole)
                .orElseThrow(() -> new IllegalStateException(
                        messageService.get("role.not_found_by_name", defaultRole)
                ));

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
            System.err.println(messageService.get("email.send_failed") + ": " + e.getMessage());
        }

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .gender(savedUser.getGender() != null
                        ? GenderEnum.fromValue(savedUser.getGender()).name().toLowerCase()
                        : null)
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
            throw new IllegalArgumentException(messageService.get("auth.username_required"));
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException(messageService.get("auth.password_required"));
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(
                        messageService.get("auth.invalid_credentials")
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(messageService.get("auth.invalid_credentials"));
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
                .gender(savedUser.getGender() != null
                        ? GenderEnum.fromValue(savedUser.getGender()).name().toLowerCase()
                        : null)
                .roleId(savedUser.getRoles() != null && !savedUser.getRoles().isEmpty()
                        ? savedUser.getRoles().iterator().next().getId()
                        : null)
                .lastLoginAt(savedUser.getLastLoginAt())
                .build();
    }

    @Override
    @Transactional
    public String changePasswordWithOtp(ChangePasswordDTO request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException(messageService.get("auth.user_id_required"));
        }

        if (request.getOtp() == null || request.getOtp().isBlank()) {
            throw new IllegalArgumentException(messageService.get("auth.otp_required"));
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new IllegalArgumentException(messageService.get("auth.new_password_required"));
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(messageService.get("auth.user_not_found")));

        UserOtp userOtp = userOtpRepository
                .findByUserIdAndPurpose(user.getId(), OtpPurpose.CHANGE_PASSWORD)
                .orElseThrow(() -> new IllegalArgumentException(messageService.get("auth.otp_not_found")));

        try {
            if (Boolean.TRUE.equals(userOtp.getUsed())) {
                throw new IllegalArgumentException(messageService.get("auth.otp_used"));
            }

            if (userOtp.getExpiresAt() == null || userOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException(messageService.get("auth.otp_expired"));
            }

            if (!request.getOtp().equals(userOtp.getOtpCode())) {
                throw new IllegalArgumentException(messageService.get("auth.otp_invalid"));
            }

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new IllegalArgumentException(messageService.get("auth.new_password_same_as_old"));
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            userOtp.setUsed(true);
            userOtp.setFailedAttempts(0);
            userOtp.setResendCount(0);
            userOtpRepository.save(userOtp);

            return messageService.get("auth.change_password_success");

        } catch (IllegalArgumentException ex) {
            Integer currentFailedAttempts = userOtp.getFailedAttempts() == null ? 0 : userOtp.getFailedAttempts();
            userOtp.setFailedAttempts(currentFailedAttempts + 1);
            userOtpRepository.save(userOtp);
            throw ex;
        }
    }
}