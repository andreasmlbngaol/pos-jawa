package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.dao.UsersDao;
import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.dto.Cashier;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.utils.PasswordManager;
import com.jawa.utsposclient.utils.SessionManager;

import java.io.IOException;

public class AuthRepository {
    public static User login(String username, String password) throws IOException {
        var userEntity = UsersDao.getUserEntityByUsername(username);
        if (userEntity == null) {
            return null;
        }

        if (PasswordManager.verifyPassword(password, userEntity.getPassword())) {
            SessionManager.saveSession(userEntity);

            return (userEntity.getRole() == Role.Admin)
                ? new Admin(userEntity)
                : new Cashier(userEntity);
        }
        return null;
    }
}
