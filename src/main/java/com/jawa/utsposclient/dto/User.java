package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.Role;

public abstract class User {
    private Long id;
    private String username;
    private String name;
    private Role role;

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
