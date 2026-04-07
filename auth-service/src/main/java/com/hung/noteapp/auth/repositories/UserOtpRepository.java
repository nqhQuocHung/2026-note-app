package com.hung.noteapp.auth.repositories;

import com.hung.noteapp.auth.enums.OtpPurpose;
import com.hung.noteapp.auth.pojos.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    Optional<UserOtp> findByUserIdAndPurpose(Long userId, OtpPurpose purpose);
}