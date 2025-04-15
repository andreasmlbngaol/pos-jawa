package com.jawa.utsposclient.controller;

import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.utils.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Optional;

public class HomeController extends Controller {
    private User user;

    @FXML private TextArea textArea;

    @FXML
    private void initialize() throws IOException {
        this.user = JawaAuth.getInstance().getCurrent();

        if(user == null) {
            System.err.println("You are not logged in. Redirect to login page.");
            switchScene(AppScene.LOGIN);
        }

        var info = String.format("""
            Username: %s
            Name: %s
            Role: %s
            """,
            user.getUsername(),
            user.getName(),
            user.getRole()
        );
        textArea.setText(info);
    }

    @FXML
    protected void onLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Strings.get("signout_alert_title"));
        alert.setHeaderText("Yakin ingin sign out?");
        alert.setContentText("Aksi ini akan mengeluarkan kamu dari sesi saat ini.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Lanjutkan sign out
            try {
                switchScene(AppScene.LOGIN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
