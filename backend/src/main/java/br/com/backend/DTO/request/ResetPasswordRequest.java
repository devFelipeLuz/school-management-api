package br.com.backend.DTO.request;

public record ResetPasswordRequest(String token, String newPassword) {
}
