package br.com.backend.domain;

import br.com.backend.domain.enums.Role;
import br.com.backend.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    private Instant deletedAt;

    @JsonIgnore
    private Instant createdAt;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
        this.deletedAt = null;
        this.createdAt = Instant.now();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = null;
        this.enabled = true;
        this.deletedAt = null;
        this.createdAt = Instant.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled && this.deletedAt == null;
    }

    public static User createGlobalUser(
            String username,
            String encodedPassword) {

        return new User(username, encodedPassword);
    }

    public static User createAdminUser(
            String username,
            String encodedPassword,
            Role role) {

        return new User(username, encodedPassword, role);
    }

    public void updateUsername(String username) {
        if (!this.enabled) {
            throw new BusinessException("Usuário desabilitado");
        }

        this.username = username;
    }

    public void updatePassword(String password) {
        if (!this.enabled) {
            throw new BusinessException("Usuário desabilitado");
        }

        this.password = password;
    }

    public void deactivate() {
        this.enabled = false;
        this.deletedAt = Instant.now();
    }
}
