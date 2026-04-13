package br.com.backend.entity.enums;

public enum Role {
    ADMIN,
    STUDENT,
    PROFESSOR,
    COORDINATOR;

    public String getAuthority() {
        return this.name();
    }
}
