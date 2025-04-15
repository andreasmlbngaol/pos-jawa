package com.jawa.utsposclient.utils;

/**
 * Tambahkan semua scene disini.
 * Biar terhindar dari kesalahan Typo dan lebih konsisten.
 */
public enum AppScene {
    LOGIN("login-view", "Login"),
    ADMIN_HOME("admin-view", "Admin Home"),
    CASHIER_HOME("cashier-view", "Cashier Home"),;

    private final String fxml;
    private final String title;

    public static final int width = 1280;
    public static final int height = 720;


    AppScene(String fxml, String title) {
        this.fxml = String.format("views/%s.fxml", fxml);
        this.title = title;
    }

    public String getFxml() {
        return fxml;
    }

    public String getTitle() {
        return title;
    }
}
