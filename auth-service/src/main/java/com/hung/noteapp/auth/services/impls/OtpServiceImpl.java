package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.dtos.ChangePasswordOtpDTO;
import com.hung.noteapp.auth.dtos.ForgotPasswordDTO;
import com.hung.noteapp.auth.dtos.OtpResponse;
import com.hung.noteapp.auth.enums.OtpPurposeEnum;
import com.hung.noteapp.auth.pojos.User;
import com.hung.noteapp.auth.pojos.UserOtp;
import com.hung.noteapp.auth.repositories.UserOtpRepository;
import com.hung.noteapp.auth.repositories.UserRepository;
import com.hung.noteapp.auth.services.EmailService;
import com.hung.noteapp.auth.services.MessageService;
import com.hung.noteapp.auth.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MessageService messageService;

    @Value("${mail.template.change-password-otp.subject}")
    private String changePasswordOtpSubject;

    @Value("${mail.template.change-password-otp.body}")
    private String changePasswordOtpBody;

    @Override
    @Transactional
    public OtpResponse createChangePasswordOtp(ChangePasswordOtpDTO request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException(messageService.get("auth.user_id_required"));
        }

        if (request.getOldPassword() == null || request.getOldPassword().isBlank()) {
            throw new IllegalArgumentException(messageService.get("auth.old_password_required"));
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(messageService.get("auth.user_not_found")));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException(messageService.get("auth.old_password_incorrect"));
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException(messageService.get("auth.user_email_required"));
        }

        String otp = generateOtp();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = now.plusMinutes(5);

        UserOtp userOtp = userOtpRepository
                .findByUserIdAndPurpose(user.getId(), OtpPurposeEnum.CHANGE_PASSWORD)
                .orElse(null);

        if (userOtp == null) {
            userOtp = UserOtp.builder()
                    .userId(user.getId())
                    .purpose(OtpPurposeEnum.CHANGE_PASSWORD)
                    .otpCode(otp)
                    .expiresAt(expiredAt)
                    .failedAttempts(0)
                    .resendCount(0)
                    .used(false)
                    .build();
        } else {
            userOtp.setOtpCode(otp);
            userOtp.setExpiresAt(expiredAt);

            if (Boolean.TRUE.equals(userOtp.getUsed())) {
                userOtp.setUsed(false);
                userOtp.setFailedAttempts(0);
                userOtp.setResendCount(0);
            } else {
                int resendCount = userOtp.getResendCount() == null ? 0 : userOtp.getResendCount();
                userOtp.setResendCount(resendCount + 1);
            }
        }

        userOtpRepository.save(userOtp);

        return new OtpResponse(
                userOtp.getUserId(),
                messageService.get("email.send_success"),
                userOtp.getOtpCode(),
                userOtp.getCreatedAt(),
                userOtp.getExpiresAt()
        );
    }

    @Override
    @Transactional
    public OtpResponse createForgotPasswordOtp(ForgotPasswordDTO request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException(getMessageSafely("auth.username_required", "Username is required"));
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(
                        getMessageSafely("auth.user_not_found", "User not found")
                ));

        String otp = generateOtp();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = now.plusMinutes(5);

        UserOtp userOtp = userOtpRepository
                .findByUserIdAndPurpose(user.getId(), OtpPurposeEnum.FORGOT_PASSWORD)
                .orElse(null);

        if (userOtp == null) {
            userOtp = UserOtp.builder()
                    .userId(user.getId())
                    .purpose(OtpPurposeEnum.FORGOT_PASSWORD)
                    .otpCode(otp)
                    .expiresAt(expiredAt)
                    .failedAttempts(0)
                    .resendCount(0)
                    .used(false)
                    .build();
        } else {
            int resendCount = userOtp.getResendCount() == null ? 0 : userOtp.getResendCount();

            userOtp.setOtpCode(otp);
            userOtp.setExpiresAt(expiredAt);
            userOtp.setUsed(false);
            userOtp.setFailedAttempts(0);
            userOtp.setResendCount(resendCount + 1);
        }

        userOtpRepository.save(userOtp);

        String message = getMessageSafely("email.send_success", "Email sent successfully")
                + " to " + user.getEmail();

        return new OtpResponse(
                userOtp.getUserId(),
                message,
                userOtp.getOtpCode(),
                userOtp.getCreatedAt(),
                userOtp.getExpiresAt()
        );
    }

    private String getMessageSafely(String key, String fallback) {
        try {
            return messageService.get(key);
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String generateOtp() {
        int number = 100000 + new Random().nextInt(900000);
        return String.valueOf(number);
    }
}