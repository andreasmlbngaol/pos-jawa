package com.jawa.utsposclient.controller;

import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.repo.AuthRepository;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class LoginController extends Controller {
    @FXML private TextArea statusTextArea;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordField;

    @FXML private void initialize() {
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLogin();
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLogin();
            }
        });
    }

    @FXML
    protected void onLogin() {
        try {
            var user = AuthRepository.login(usernameTextField.getText(), passwordField.getText());

            if (user != null) {
                if(user instanceof Admin) {
                    switchScene("/admin-view.fxml", "Admin View");
                } else {
                    switchScene("/cashier-view.fxml", "Admin View");
                }

            } else {
                statusTextArea.setText("Invalid username or password");
            }
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

//    @FXML
//    protected void onLogin() {
//        try {
//            var result = AuthRepository.login(usernameTextField.getText(), passwordField.getText());
//
//            if(result.isSuccess()) {
//                SessionManager.saveLocalSession(ApiClient.getInstance().getCookieManager());
//
//                var user = result.getData();
//                if(user.getRole() == Role.Admin) {
//                    // Go to Admin Menu
//                    switchScene("/admin-view.fxml", "Admin View");
//                } else {
//                    // Go to Cashier Menu
//                    switchScene("/cashier-view.fxml", "Cashier View");
//                }
//            } else {
//                statusTextArea.setText(result.getMessage());
//            }
//        } catch (IOException | RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
}