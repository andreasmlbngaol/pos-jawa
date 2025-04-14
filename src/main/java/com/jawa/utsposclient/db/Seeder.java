package com.jawa.utsposclient.db;

import com.jawa.utsposclient.entities.Users;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.utils.Config;
import com.jawa.utsposclient.utils.PasswordManager;

public class Seeder {
    public static void seedAdmin() {
        Database.executeVoidTransaction(session -> {
            Long userCount = session.createQuery(
                "SELECT count(u) FROM Users u",
                Long.class
            )
                .getSingleResult();

            if(userCount == 0) {
                Users admin = new Users();
                var username = Config.getProperty("admin.username");
                var name = Config.getProperty("admin.name");
                var hashedPassword = PasswordManager.hashPassword(Config.getProperty("admin.password"));

                admin.setUsername(username);
                admin.setName(name);
                admin.setPassword(hashedPassword);
                admin.setRole(Role.ADMIN);

                session.persist(admin);
                System.out.println("Admin created");
            }
        });
    }
}
