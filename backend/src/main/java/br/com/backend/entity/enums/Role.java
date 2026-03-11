package br.com.backend.entity.enums;

public enum Role {
    ADMIN,
    STUDENT,
    PROFESSOR,
    CORDINATOR;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
