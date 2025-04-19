package com.jawa.utsposclient.views.cashier;

import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.views.Controller;
import com.jawa.utsposclient.views.UserController;
import javafx.fxml.FXML;

import java.io.IOException;

public class CashierController extends Controller implements UserController {
    @FXML
    private void onBackToHome() throws IOException {
        switchScene(AppScene.CASHIER_HOME);
    }
}
