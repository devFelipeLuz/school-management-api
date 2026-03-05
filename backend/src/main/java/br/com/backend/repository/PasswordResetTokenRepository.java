package br.com.backend.repository;

import br.com.backend.domain.PasswordResetToken;
import br.com.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    @Modifying
    @Query("""
    UPDATE PasswordResetToken t
    SET t.used = true
    WHERE t.user = :user
    """)
    void markAllAsUsedByUser(User user);

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

}
