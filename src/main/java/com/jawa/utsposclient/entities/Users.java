package com.jawa.utsposclient.entities;

import com.jawa.utsposclient.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_username", columnList = "username")
    }
)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, name="must_change_password")
    private boolean mustChangePassword;

    @Column(nullable = false, name="is_active")
    private boolean isActive;

    public Users() {}

    public void setUsername(String username) {
        this.username = username.trim().toLowerCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public void setActive(boolean active) {
        this.isActive = active;
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

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    @SuppressWarnings("unused")
    public boolean isActive() {
        return isActive;
    }
}