package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.Role;

public class Cashier extends User {
    public Cashier(User user) {
        super(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.isMustChangePassword(),
            Role.ADMIN
        );
    }
}
