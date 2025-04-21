package com.jawa.utsposclient.views.admin;

import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;

public class AdminHomeController extends AdminController {

    @FXML private VBox productVBox;
    @FXML private VBox userVBox;
    @FXML private VBox historyVBox;

    @FXML private Button logoutButton;

    @FXML
    private void initialize() throws IOException {
        Admin admin = (Admin) JawaAuth.getInstance().getCurrent();

        if(admin == null) {
            System.err.println("You are not logged in. Redirect to login page.");
            switchScene(AppScene.LOGIN);


        }

        logoutButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_LOGOUT,
            StringRes.get("logout_button"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(productVBox);
        addHoverEffect(userVBox);
        addHoverEffect(historyVBox);
        addHoverEffect(logoutButton);
    }

    @FXML
    private void onSwitchToManageProduct() throws IOException {
        switchScene(AppScene.MANAGE_PRODUCT);
    }

    @FXML
    private void onSwitchToManageUser() throws IOException {
        switchScene(AppScene.MANAGE_USER);
    }
}
