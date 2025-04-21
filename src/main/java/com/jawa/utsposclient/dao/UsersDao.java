package com.jawa.utsposclient.dao;

import  com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.entities.Users;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.utils.PasswordManager;

import java.util.List;


public class UsersDao {
    public static Users getUserEntityByUsername(String username) {
        return Database.executeTransaction(session -> {
            var query = session.createQuery(
                "FROM Users u WHERE u.username = :username",
                Users.class
            );
            query.setParameter("username", username);

            return query.uniqueResult();
        });
    }

    public static void updatePassword(Long id, String newPassword, boolean mustChangePassword) {
        Database.executeVoidTransaction(session -> {
            Users user = session.get(Users.class, id);
            if(user != null) {
                user.setPassword(PasswordManager.hashPassword(newPassword));
                user.setMustChangePassword(mustChangePassword);
            } else {
                throw new IllegalArgumentException("User not found");
            }
        });
    }

    public static List<Users> getAllUserEntities() {
        return Database.executeTransaction(session ->
            session.createQuery("FROM Users u WHERE u.isActive = true ORDER BY u.id", Users.class).getResultList()
        );
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

    private static void insertUser(Users user) {
        Database.executeVoidTransaction(session -> session.persist(user));
    }

    public static void insertCashier(String username, String name, String password) {
        Users cashier = new Users();
        cashier.setUsername(username);
        cashier.setName(name);
        cashier.setPassword(PasswordManager.hashPassword(password));
        cashier.setRole(Role.Cashier);
        cashier.setMustChangePassword(true);
        cashier.setActive(true);
        insertUser(cashier);
    }

    public static void setUserInactive(Long id) {
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

    public static void updateName(Long id, String name) {
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
