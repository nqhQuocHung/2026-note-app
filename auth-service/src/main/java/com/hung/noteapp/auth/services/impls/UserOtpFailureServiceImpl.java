package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.pojos.UserOtp;
import com.hung.noteapp.auth.repositories.UserOtpRepository;
import com.hung.noteapp.auth.services.MessageService;
import com.hung.noteapp.auth.services.UserOtpFailureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserOtpFailureServiceImpl implements UserOtpFailureService {

    private final UserOtpRepository userOtpRepository;
    private final MessageService messageService;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseFailedAttempts(Long userOtpId) {
        UserOtp userOtp = userOtpRepository.findById(userOtpId)
                .orElseThrow(() -> new IllegalArgumentException(messageService.get("auth.otp_not_found")));

        int current = userOtp.getFailedAttempts() == null ? 0 : userOtp.getFailedAttempts();
        userOtp.setFailedAttempts(current + 1);

        userOtpRepository.save(userOtp);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseFailedAttemptsAndLockIfNeeded(Long userOtpId, int maxAttempts) {
        UserOtp userOtp = userOtpRepository.findById(userOtpId)
                .orElseThrow(() -> new IllegalArgumentException("UserOtp not found"));

        int current = userOtp.getFailedAttempts() == null ? 0 : userOtp.getFailedAttempts();
        int updated = current + 1;

        userOtp.setFailedAttempts(updated);

        if (updated >= maxAttempts) {
            userOtp.setUsed(true);
        }

        userOtpRepository.saveAndFlush(userOtp);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetFailedAttempts(Long userOtpId) {
        UserOtp userOtp = userOtpRepository.findById(userOtpId)
                .orElseThrow(() -> new IllegalArgumentException(messageService.get("auth.otp_not_found")));

        userOtp.setFailedAttempts(0);
        userOtpRepository.save(userOtp);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetFailedAttemptsAndMarkUsed(Long userOtpId) {
        UserOtp userOtp = userOtpRepository.findById(userOtpId)
                .orElseThrow(() -> new IllegalArgumentException(messageService.get("auth.otp_not_found")));

        userOtp.setUsed(true);
        userOtp.setFailedAttempts(0);
        userOtp.setResendCount(0);

        userOtpRepository.save(userOtp);
    }
}