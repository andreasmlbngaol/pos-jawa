package com.jawa.utsposclient.views;

import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;

public class SetPasswordController extends Controller {

    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private Button saveButton;

    @FXML
    private void initialize() {
        saveButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_CONTENT_SAVE,
            StringRes.get("save_label"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(saveButton);
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
            LogsDao.setPassword(user.getId());

            if(user.getRole() == Role.Admin) {
                switchScene(AppScene.ADMIN_HOME);
            } else {
                switchScene(AppScene.CASHIER_HOME);
            }
        }
    }
}
