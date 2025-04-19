package com.jawa.utsposclient.utils;

import com.jawa.utsposclient.dto.User;

public class JawaAuth {
    private static JawaAuth instance;
    private User current;
    private String token;

    private JawaAuth() {}

    public static JawaAuth getInstance() {
        if (instance == null) {
            instance = new JawaAuth();
        }
        return instance;
    }

    public User getCurrent() {
        return current;
    }

    public void login(User user, String token) {
        current = user;
        this.token = token;
    }

    public void logout() {
        current = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
