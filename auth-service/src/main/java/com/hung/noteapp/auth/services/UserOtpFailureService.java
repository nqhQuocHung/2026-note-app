package com.hung.noteapp.auth.services;

public interface UserOtpFailureService {

    void increaseFailedAttempts(Long userOtpId);

    void increaseFailedAttemptsAndLockIfNeeded(Long userOtpId, int maxAttempts);

    void resetFailedAttempts(Long userOtpId);

    void resetFailedAttemptsAndMarkUsed(Long userOtpId);
}