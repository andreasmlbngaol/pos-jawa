package com.jawa.utsposclient.utils;

import com.jawa.utsposclient.dto.User;

public class JawaAuth {
    private static JawaAuth instance;
    private User current;

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

    public void login(User user) {
        current = user;
    }

    public void logout() {
        current = null;
    }
}
