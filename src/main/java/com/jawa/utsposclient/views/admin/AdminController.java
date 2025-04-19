package com.jawa.utsposclient.views.admin;

import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.views.Controller;
import com.jawa.utsposclient.views.UserController;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminController extends Controller implements UserController {
    @FXML
    private void onBackToHome() throws IOException {
        switchScene(AppScene.ADMIN_HOME);
    }
}
