package com.jawa.utsposclient.views;

import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class SetPasswordController extends Controller {

    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;

    @FXML
    private void initialize() {
        password.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                try {
                    onSetPassword();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        confirmPassword.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                try {
                    onSetPassword();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    private void onSetPassword() throws IOException {
        var user = JawaAuth.getInstance().getCurrent();
        if(password.getText().equals(confirmPassword.getText())) {
            JawaAuth.getInstance().getCurrent().setPassword(password.getText());

            if(user.getRole() == Role.Admin) {
                switchScene(AppScene.ADMIN_HOME);
            } else {
                switchScene(AppScene.CASHIER_HOME);
            }
        }
    }
}
