package com.jawa.utsposclient.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

public class PasswordManager {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public static String generateOtp() {
        StringBuilder otp = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = new SecureRandom().nextInt(CHARACTERS.length());
            otp.append(CHARACTERS.charAt(index));
        }
        return otp.toString();
    }
}
