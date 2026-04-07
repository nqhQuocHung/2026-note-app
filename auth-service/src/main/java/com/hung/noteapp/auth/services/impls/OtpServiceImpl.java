package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.dtos.ChangePasswordOtpRequest;
import com.hung.noteapp.auth.dtos.OtpResponse;
import com.hung.noteapp.auth.enums.OtpPurpose;
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
    public OtpResponse createChangePasswordOtp(ChangePasswordOtpRequest request) {
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
                .findByUserIdAndPurpose(user.getId(), OtpPurpose.CHANGE_PASSWORD)
                .orElse(null);

        if (userOtp == null) {
            userOtp = UserOtp.builder()
                    .userId(user.getId())
                    .purpose(OtpPurpose.CHANGE_PASSWORD)
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
                messageService.get("email.send_success"),
                userOtp.getOtpCode(),
                userOtp.getCreatedAt(),
                userOtp.getExpiresAt()
        );
    }

    private String generateOtp() {
        int number = 100000 + new Random().nextInt(900000);
        return String.valueOf(number);
    }
}