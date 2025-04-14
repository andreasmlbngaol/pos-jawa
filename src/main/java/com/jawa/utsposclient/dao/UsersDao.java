package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.entities.Users;
import org.hibernate.query.Query;


public class UsersDao {
    public static Users getUserByUsername(String username) {
        return Database.executeTransaction(session -> {
            Query<com.jawa.utsposclient.entities.Users> query = session.createQuery(
                "FROM Users u WHERE u.username = :username",
                com.jawa.utsposclient.entities.Users.class
            );
            query.setParameter("username", username);

            return query.uniqueResult();
        });
    }
}
