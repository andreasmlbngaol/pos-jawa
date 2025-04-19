package com.jawa.utsposclient.dto;


import com.jawa.utsposclient.entities.Users;
import com.jawa.utsposclient.enums.Role;

public class Cashier extends User {
    public Cashier(
        Long id,
        String username,
        String name,
        boolean mustChangePassword
    ) {
        super(id, username, name, Role.Cashier, mustChangePassword);
    }

    public Cashier(Users user) {
        super(user.getId(), user.getUsername(), user.getName(), Role.Cashier, user.isMustChangePassword());
    }
}
