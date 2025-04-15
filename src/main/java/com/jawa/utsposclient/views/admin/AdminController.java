package com.jawa.utsposclient.views.admin;

import com.jawa.utsposclient.utils.AppScene;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminController extends Controller {
    @FXML
    protected void onBackToHome() throws IOException {
        switchScene(AppScene.ADMIN_HOME);
    }
}
