package com.jawa.utsposclient.utils;

import com.jawa.utsposclient.dao.UserSessionsDao;
import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.dto.Cashier;
import com.jawa.utsposclient.entities.Users;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.UUID;

public class SessionManager {
    private static final String SESSION_FILE = "session.properties";

    private static String generateSessionToken() {
        return UUID.randomUUID().toString();
    }

    public static void saveSession(Users user) {
        try {
            var token = generateSessionToken();
            String encryptedToken = CryptoUtils.encrypt(token);
            Properties prop = new Properties();

            prop.setProperty("token", encryptedToken);
            try (var out = new FileOutputStream(SESSION_FILE)) {
                prop.store(out, "Session");
            } catch (Exception e) {
                throw new RuntimeException("Error saving session file", e);
            }

            JawaAuth.getInstance().setToken(token);

            UserSessionsDao.insertNewSession(user, token);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting session token", e);
        }
    }

    public static boolean loadSession() throws Exception {
        File file = new File(SESSION_FILE);
        if(!file.exists()) return false;

        Properties prop = new Properties();
        try(var in = new FileInputStream(file)) {
            prop.load(in);
        }
        var token = CryptoUtils.decrypt(prop.getProperty("token"));

        var user = UserRepository.getUserBySessionToken(token);
        if(user != null) {
            if(user.getRole() == Role.Admin) {
                var admin = new Admin(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.isMustChangePassword()
                );
                JawaAuth.getInstance().login(admin, token);
            } else {
                var cashier = new Cashier(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.isMustChangePassword()
                );
                JawaAuth.getInstance().login(cashier, token);
            }
            return true;
        }
        return false;
    }

    public static boolean clearSession() {
        var token = JawaAuth.getInstance().getToken();
        UserSessionsDao.deleteSession(token);

        return new File(SESSION_FILE).delete();
    }

}
