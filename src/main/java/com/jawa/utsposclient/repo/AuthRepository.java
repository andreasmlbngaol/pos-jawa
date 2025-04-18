package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.dao.UsersDao;
import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.dto.Cashier;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.utils.PasswordManager;

import java.io.IOException;

public class AuthRepository {
    public static User login(String username, String password) throws IOException {
        return Database.executeTransaction(session -> {
            var user = UsersDao.getUserByUsername(username);
            if (user == null) {
                return null;
            }

            if(PasswordManager.verifyPassword(password, user.getPassword())) {
                var entityId = user.getId();
                var entityUsername = user.getUsername();
                var entityName = user.getName();
                var entityRole = user.getRole();
                var entityChangePassword = user.isMustChangePassword();

                return (entityRole == Role.Admin)
                    ? new Admin(entityId, entityUsername, entityName, entityChangePassword)
                    : new Cashier(entityId, entityUsername, entityName, entityChangePassword);
            }
            return null;
        });
    }

//    public static ApiResponse<Void> logout() throws IOException {
//        return apiClient.postParsed("/logout");
//    }
}
