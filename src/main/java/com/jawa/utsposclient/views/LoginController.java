package com.jawa.utsposclient.views;

import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.repo.AuthRepository;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
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
                JawaAuth.getInstance().login(user);
                // Tambah session belakangan

                if(user.isMustChangePassword()) {
                  switchScene(AppScene.SET_PASSWORD);
                } else if(user instanceof Admin) {
                switchScene(AppScene.ADMIN_HOME);
                } else {
                    switchScene(AppScene.CASHIER_HOME);
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
//                    switchScene("/admin-home-view.fxml", "Admin View");
//                } else {
//                    // Go to Cashier Menu
//                    switchScene("/cashier-home-view.fxml", "Cashier View");
//                }
//            } else {
//                statusTextArea.setText(result.getMessage());
//            }
//        } catch (IOException | RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }
}