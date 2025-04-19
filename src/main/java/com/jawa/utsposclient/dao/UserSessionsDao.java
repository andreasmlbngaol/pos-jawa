package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.entities.UserSessions;
import com.jawa.utsposclient.entities.Users;

public class UserSessionsDao {
    public static Users getUserByToken(String token) {
        return Database.executeTransaction(session -> {
           var query = session.createQuery(
               "SELECT s.user FROM UserSessions s WHERE s.token = :token",
               Users.class
           );
           query.setParameter("token", token);
           return query.uniqueResult();
        });
    }

    public static void insertNewSession(Users user, String token) {
        Database.executeVoidTransaction(session -> {
            var newSession = new UserSessions();
            newSession.setUser(user);
            newSession.setToken(token);
            session.persist(newSession);
        });
    }

    public static void deleteSession(String token) {
        Database.executeVoidTransaction(session -> {
            var query = session.createMutationQuery("DELETE FROM UserSessions s WHERE s.token = :token");
            query.setParameter("token", token);
            query.executeUpdate();
        });
    }
}
