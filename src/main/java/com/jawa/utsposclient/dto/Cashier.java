package com.jawa.utsposclient.dto;


import com.jawa.utsposclient.enums.Role;

public class Cashier extends User {
    public Cashier(
        Long id,
        String username,
        String name
    ) {
        super(id, username, name, Role.Cashier);
    }
}
