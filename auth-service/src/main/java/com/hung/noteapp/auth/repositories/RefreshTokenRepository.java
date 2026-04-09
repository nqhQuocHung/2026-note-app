package com.hung.noteapp.auth.repositories;

import com.hung.noteapp.auth.enums.TokenTypeEnum;
import com.hung.noteapp.auth.pojos.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    List<RefreshToken> findByUserIdAndTokenTypeAndRevokedAtIsNull(Long userId, TokenTypeEnum tokenType);

    Optional<RefreshToken> findByTokenHashAndTokenType(String tokenHash, TokenTypeEnum tokenType);

    Optional<RefreshToken> findByTokenHashAndTokenTypeAndRevokedAtIsNull(
            String tokenHash,
            TokenTypeEnum tokenType
    );
}