package com.jawa.utsposclient.utils;

import java.util.ResourceBundle;

public class Strings {
    private static final ResourceBundle res = ResourceBundle.getBundle("com.jawa.utsposclient.strings");

    public static String get(String key) {
        return res.getString(key);
    }

    public static ResourceBundle getBundle() {
        return res;
    }
}
