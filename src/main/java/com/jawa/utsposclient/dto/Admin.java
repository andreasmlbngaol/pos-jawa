package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.UserRepository;

import java.io.IOException;

public class Admin extends User {

    public Admin(
        Long id,
        String username,
        String name
    ) {
        super(id, username, name, Role.Admin);
    }

    public String resetPassword(long id) throws IOException {
        var result = UserRepository.resetPassword(id);
        if(result.isSuccess()) {
            return result.getData().otp();
        }
        return result.getMessage();
    }
}
