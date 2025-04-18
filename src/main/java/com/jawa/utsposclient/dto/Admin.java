package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.Role;


public class Admin extends User {

    public Admin(
        Long id,
        String username,
        String name,
        boolean mustChangePassword
    ) {
        super(id, username, name, Role.Admin, mustChangePassword);
    }
//    public String resetPassword(long id) throws IOException {
//        var result = UserRepository.resetPassword(id);
//        if(result.isSuccess()) {
//            return result.getData().otp();
//        }
//        return result.getMessage();
//    }
}
