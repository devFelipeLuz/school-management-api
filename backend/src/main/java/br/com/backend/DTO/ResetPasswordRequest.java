package br.com.backend.DTO;

public record ResetPasswordRequest(String token, String newPassword) {
}
