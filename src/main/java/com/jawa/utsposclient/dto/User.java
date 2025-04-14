package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.Role;

public abstract class User {
    private final Long id;
    private final String username;
    private final String name;
    private final Role role;

    public User(
        Long id,
        String username,
        String name,
        Role role
    ) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
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
}
