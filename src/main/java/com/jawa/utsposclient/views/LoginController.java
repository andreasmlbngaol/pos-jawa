package com.jawa.utsposclient.views;

import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.repo.AuthRepository;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.JawaButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;

public class LoginController extends Controller {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;

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


        loginButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_LOGIN,
            "Login",
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(loginButton);
    }

    @FXML
    protected void onLogin() {
        try {
            var username = usernameTextField.getText().trim().toLowerCase();
            var password = passwordField.getText().trim();

            var user = AuthRepository.login(username, password);

            if (user != null) {
                JawaAuth.getInstance().login(user, JawaAuth.getInstance().getToken());
                LogsDao.login(user.getId());

                if(user.isMustChangePassword()) {
                  switchScene(AppScene.SET_PASSWORD);
                } else if(user instanceof Admin) {
                switchScene(AppScene.ADMIN_HOME);
                } else {
                    switchScene(AppScene.CASHIER_HOME);
                }
            } else {
//                JavaFXExt.showStyledAlert(Alert.AlertType.ERROR, "Login Failed", "Username atau password salah!");
                FramelessStyledAlert.show("Login Failed", "Username atau Password salah!");
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