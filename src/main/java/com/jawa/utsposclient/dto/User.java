package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.entities.Users;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.UserRepository;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.SessionManager;

public class User {
    private final Long id;
    private final String username;
    private final String name;
    private final Role role;
    private final boolean mustChangePassword;

    public User(
        Long id,
        String username,
        String name,
        Role role,
        boolean mustChangePassword
    ) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
        this.mustChangePassword = mustChangePassword;
    }

    public User(Users user) {
        this(user.getId(), user.getUsername(), user.getName(), user.getRole(), user.isMustChangePassword());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setPassword(String password) {
        UserRepository.setPassword(id, password);
    }

    public void logout() {
        if(!SessionManager.clearSession()) {
            throw new RuntimeException("Session not Found");
        }
        JawaAuth.getInstance().logout();
    }
}
