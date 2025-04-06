package com.jawa.utsposclient;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    private final ApiClient apiClient = ApiClient.getInstance();

    @FXML private TextArea statusTextArea;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;

    @FXML
    protected void onLoginButtonClick() {
        try {
            var result = apiClient.post(
                    "/login",
                    new LoginRequest(usernameTextField.getText(), passwordTextField.getText())
            );
            if(result != null) {
                if(result.code() == 200) {
                    usernameTextField.setText("");
                    passwordTextField.setText("");
                    SessionManager.saveLocalSession(ApiClient.getInstance().getCookieManager());
                }
                if(result.body() != null) {
                    statusTextArea.setText(result.body().string());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        try {
            var result = apiClient.get("/hello");

            if(result != null) {
                if(result.code() == 200 && result.body() != null) {
                    statusTextArea.setText(result.body().string());
                } else {
                    var code = result.code();
                    if(result.body() != null) {
                        statusTextArea.setText(code + ": " + result.body().string());
                    }
                    SessionManager.clearLocalSession(ApiClient.getInstance().getCookieManager());
                }
            }
        } catch (IOException e) {
            System.out.println("[SessionManager] Failed to load session: " + e.getMessage());
        }
    }

    @FXML
    protected void onClearSession() {
        SessionManager.clearLocalSession(ApiClient.getInstance().getCookieManager());
    }
}