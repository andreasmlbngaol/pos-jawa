package com.jawa.utsposclient.views.admin;

import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class AdminHomeController extends AdminController {
    private Admin user;

    @FXML private TextArea textArea;

    @FXML
    private void initialize() throws IOException {
        this.user = (Admin) JawaAuth.getInstance().getCurrent();

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
    private void onSwitchToManageProduct() throws IOException {
        switchScene(AppScene.MANAGE_PRODUCT);
    }

    @FXML
    private void onSwitchToManageUser() throws IOException {
        switchScene(AppScene.MANAGE_USER);
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
