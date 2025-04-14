package com.jawa.utsposclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController extends Controller {
    public Button signOutButton;

    @FXML
    protected void onSignOutButtonClick() {
        try {
            switchScene("/login-view.fxml", "Login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @FXML
//    protected void onSignOutButtonClick() {
//        try {
//            var result = AuthRepository.logout();
//
//            if(result.isSuccess()) SessionManager.clearLocalSession(apiClient.getCookieManager());
//            else System.out.println(result.getMessage());
//
//            switchScene("/login-view.fxml", "Login");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
