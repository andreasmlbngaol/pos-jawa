package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.dao.UserSessionsDao;
import com.jawa.utsposclient.dao.UsersDao;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.utils.PasswordManager;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    public static void setPassword(long userId, String newPassword) {
        UsersDao.updatePassword(userId, newPassword, false);
    }

    public static String resetPasswordAndGetOtp(long userId) {
        var otp = PasswordManager.generateOtp();
        UsersDao.updatePassword(userId, otp, true);
        return otp;
    }

    public static List<User> getAllUsers() {
        return UsersDao.getAllUserEntities().stream()
            .map(user -> new User(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole(),
                user.isMustChangePassword()
            ))
            .collect(Collectors.toList());
    }

    public static User getUserBySessionToken(String token) {
        var user = UserSessionsDao.getUserByToken(token);
        if(user == null) return null;

        return new User(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.getRole(),
            user.isMustChangePassword()
        );
    }

    public static boolean checkUsername(String username) {
        return UsersDao.isUsernameTaken(username);
    }

    public static String addCashierAndGetOtp(String username, String name) {
        var otp = PasswordManager.generateOtp();
        UsersDao.insertCashier(username, name, otp);
        return otp;
    }

    public static void softDelete(long id) {
        UsersDao.setUserInactive(id);
    }

    public static void changeName(long id, String name) {
        UsersDao.updateName(id, name);
    }
}