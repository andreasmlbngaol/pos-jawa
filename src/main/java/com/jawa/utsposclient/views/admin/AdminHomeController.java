package com.jawa.utsposclient.views.admin;

import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminHomeController extends AdminController {

    @FXML
    private void initialize() throws IOException {
        Admin admin = (Admin) JawaAuth.getInstance().getCurrent();

        if(admin == null) {
            System.err.println("You are not logged in. Redirect to login page.");
            switchScene(AppScene.LOGIN);
        }
    }

    @FXML
    private void onSwitchToManageProduct() throws IOException {
        switchScene(AppScene.MANAGE_PRODUCT);
    }

    @FXML
    private void onSwitchToManageUser() throws IOException {
        switchScene(AppScene.MANAGE_USER);
    }
}
