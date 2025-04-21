package com.jawa.utsposclient.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class StringRes {
    private static ResourceBundle res = ResourceBundle.getBundle("com.jawa.utsposclient.strings");

    public static void setRes(Locale locale) {
        res = ResourceBundle.getBundle("com.jawa.utsposclient.strings", locale);
    }

    public static String get(String key) {
        return res.getString(key);
    }

    public static String getFormatted(String key, Object... args) {
        return String.format(get(key), args);
    }

    public static ResourceBundle getBundle() {
        return res;
    }
}
