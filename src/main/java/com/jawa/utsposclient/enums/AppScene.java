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
    ADD_PRODUCT_DIALOG("fragment/add-product-dialog-fragment", "Add Product"),

    CASHIER_HOME("cashier/cashier-home-view", "Cashier Home"),
    PURCHASE_TRANSACTION("cashier/purchase-transaction-view", "Purchase Transaction"),
    REFUND_TRANSACTION("cashier/refund-transaction-view", "Refund Transaction"),
    EDIT_PERISHABLE_DIALOG("fragment/edit-perishable-dialog-fragment", "Edit Perishable Product"),
    EDIT_NON_PERISHABLE_DIALOG("fragment/edit-non-perishable-dialog-fragment", "Edit Non Perishable Product"),
    EDIT_BUNDLE_DIALOG("fragment/edit-bundle-dialog-fragment", "Edit Bundle Product"),
    EDIT_DIGITAL_DIALOG("fragment/edit-digital-dialog-fragment", "Edit Digital Product"),
    PURCHASE_BILL("fragment/purchase-bill-jawa-people", "Purchase Bill")

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
