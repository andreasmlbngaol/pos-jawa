package com.jawa.utsposclient.enums;

/**
 * Tambahkan semua scene disini.
 * Biar terhindar dari kesalahan Typo dan lebih konsisten.
 */
public enum AppScene {
    LOGIN("login-view", "Login Scene"),
    SET_PASSWORD("set-password-view", "Set Password"),

    ADMIN_HOME("admin/admin-home-view", "Admin Home"),
    MANAGE_PRODUCT("admin/manage-product-view", "Manage Product"),
    MANAGE_USER("admin/manage-user-view", "Manage User"),
    ADD_CASHIER_DIALOG("fragment/add-cashier-dialog-fragment", "Add Cashier"),
    ADD_PRODUCT("admin/add-product-view", "Add Product"),

    CASHIER_HOME("cashier/cashier-home-view", "Cashier Home"),

    ;

    private final String fxml;
    private final String title;

    public static final int width = 1100;
    public static final int height = 620;


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
