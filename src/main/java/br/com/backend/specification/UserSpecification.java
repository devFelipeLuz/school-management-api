package br.com.backend.specification;

import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> withEmail(String email) {
        return (root, query, cb) -> {

            if (email == null || email.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"
            );
        };
    }

    public static Specification<User> withRole(Role role) {
        return (root, query, cb) -> {

            if (role == null) {
                return null;
            }

            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<User> isEnabled(Boolean enabled) {
        return (root, query, cb) -> {

            if (enabled == null) {
                return null;
            }

            return cb.equal(root.get("enabled"), enabled);
        };
    }
}
