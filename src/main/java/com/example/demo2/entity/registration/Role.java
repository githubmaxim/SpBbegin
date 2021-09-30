package com.example.demo2.entity.registration;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum with roles.
 *
 * @author Maxim
 * @version 1.0
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    /**
     * I don't know why this method is needed to register clients
     * @return null
     */
    @Override
    public String getAuthority() {
        return null;
    }
}
