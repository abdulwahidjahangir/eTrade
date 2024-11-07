package com.backend.eTrade.repositories.users;

import com.backend.eTrade.models.users.UserVerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerifyTokenRepository extends JpaRepository<UserVerifyToken, Long> {
    Optional<UserVerifyToken> findByToken(String token);
}
