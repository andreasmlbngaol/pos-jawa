package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.UserRepository;

import java.io.IOException;

public class User {
    private final long id;
    private final String username;
    private final String name;
    private final boolean mustChangePassword;
    private final Role role;

    public User(long id, String username, String name, boolean mustChangePassword, Role role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.mustChangePassword = mustChangePassword;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public Role getRole() {
        return role;
    }

    @SuppressWarnings("unused")
    public String changePassword(String oldPassword, String newPassword) throws IOException {
        return UserRepository.changePassword(id, oldPassword, newPassword).getMessage();
    }
}
