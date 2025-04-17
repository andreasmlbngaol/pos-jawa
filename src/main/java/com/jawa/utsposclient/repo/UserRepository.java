package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.entities.Users;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.utils.PasswordManager;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    public static void setPassword(long userId, String newPassword) {
        Database.executeVoidTransaction(session -> {
            Users user = session.get(Users.class, userId);
            if(user != null) {
                user.setPassword(PasswordManager.hashPassword(newPassword));
                user.setMustChangePassword(false);
            } else {
                throw new IllegalArgumentException("User not found");
            }
        });
    }

    public static String resetPassword(long userId) {
        return Database.executeTransaction(session -> {
            Users user = session.get(Users.class, userId);
            if(user != null) {
                var otp = PasswordManager.generateOtp();
                user.setPassword(PasswordManager.hashPassword(otp));
                user.setMustChangePassword(true);

                return otp;
            } else {
                throw new IllegalArgumentException("User not found");
            }
        });
    }

    public static List<User> getAllUsers() {
        return Database.executeTransaction(session -> {
            List<Users> userEntities = session.createQuery("FROM Users u WHERE u.isActive = true", Users.class).getResultList();
            return userEntities.stream()
                .map(user -> new User(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getRole(),
                    user.isMustChangePassword()
                ))
                .collect(Collectors.toList());
        });
    }

    public static boolean isUsernameTaken(String username) {
        return Database.executeTransaction(session -> {
            Long count = session.createQuery(
                "SELECT COUNT(u) FROM Users u WHERE u.username = :username AND u.isActive = true",
                Long.class
            )
                .setParameter("username", username)
                .uniqueResult();
            return count != null && count > 0;
        });
    }

    public static String addCashierAndGetOtp(String username, String name) {
        return Database.executeTransaction( session -> {
            var otp = PasswordManager.generateOtp();

            Users cashier = new Users();
            cashier.setUsername(username);
            cashier.setName(name);
            cashier.setPassword(PasswordManager.hashPassword(otp));
            cashier.setRole(Role.Cashier);
            cashier.setMustChangePassword(true);
            cashier.setActive(true);
            session.persist(cashier);
            return otp;
        });
    }

    public static void softDelete(long id) {
        Database.executeVoidTransaction(session -> {
            Users user = session.get(Users.class, id);
            if(user != null) {
                user.setActive(false);
                user.setUsername(String.format("%s-%d-deleted", user.getUsername(), user.getId()));
            } else {
                throw new IllegalArgumentException("User not found");
            }
        });
    }

    public static void updateName(long id, String name) {
        Database.executeVoidTransaction(session -> {
            Users user = session.get(Users.class, id);
            if(user != null) {
                user.setName(name);
            } else {
                throw new IllegalArgumentException("User not found");
            }
        });
    }
}



//import com.jawa.utsposclient.api.ApiResponse;
//import com.jawa.utsposclient.api.payload.ChangePasswordRequest;
//import com.jawa.utsposclient.response.OtpResponse;
//
//import java.io.IOException;
//
//public class UserRepository extends Repository {
//    public static ApiResponse<Void> changePassword(long userId, String oldPassword, String newPassword) throws IOException {
//        String path =  String.format("/users/%d/password/change", userId);
//        var request = new ChangePasswordRequest(oldPassword, newPassword);
//        var result = apiClient.patchParsed(path, request);
//        System.out.println(result.getMessage());
//        return result;
//    }
//
//    public static ApiResponse<OtpResponse> resetPassword(long userId) throws IOException {
//        String path = String.format("/users/%d/password/reset", userId);
//        return apiClient.patchParsed(path, OtpResponse.class);
//    }
//}
