package com.hung.noteapp.auth.pojos;

import com.hung.noteapp.auth.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_otps",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_purpose", columnNames = {"user_id", "purpose"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOtp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "purpose", nullable = false)
    private OtpPurpose purpose;

    @Column(name = "otp_code", nullable = false, length = 255)
    private String otpCode;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    @Column(name = "failed_attempts", nullable = false)
    private Integer failedAttempts = 0;

    @Builder.Default
    @Column(name = "resend_count", nullable = false)
    private Integer resendCount = 0;

    @Builder.Default
    @Column(name = "used", nullable = false)
    private Boolean used = false;
}