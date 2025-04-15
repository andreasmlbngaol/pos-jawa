package com.jawa.utsposclient.views;

import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.UserRepository;
import com.jawa.utsposclient.utils.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class SetPasswordController extends Controller {

    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;

    @FXML
    private void onSetPassword() throws IOException {
        var user = JawaAuth.getInstance().getCurrent();
        if(password.getText().equals(confirmPassword.getText())) {
            UserRepository.setPassword(user.getId(), password.getText());

            if(user.getRole() == Role.Admin) {
                switchScene(AppScene.ADMIN_HOME);
            } else {
                switchScene(AppScene.CASHIER_HOME);
            }
        }
    }
}
