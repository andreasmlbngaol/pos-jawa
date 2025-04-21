package com.jawa.utsposclient.views;

import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.repo.AuthRepository;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;
import java.util.Locale;

public class LoginController extends Controller {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    @FXML private HBox footerContainer;

    @FXML private void initialize() {
        ComboBox<Locale> languageDropdown = new ComboBox<>();
        languageDropdown.getItems().addAll(
            Locale.of("en"),
            Locale.of("id")
        );
        languageDropdown.setValue(Locale.getDefault());

        languageDropdown.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Locale locale, boolean empty) {
                super.updateItem(locale, empty);
                if (empty || locale == null) {
                    setText(null);
                } else {
                    setText(switch (locale.getLanguage()) {
                        case "id" -> "Indonesia";
                        case "en" -> "English";
                        default -> locale.getDisplayLanguage(locale);
                    });
                }
            }
        });

        languageDropdown.setStyle(
            "-fx-background-color: #e8b323;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
        );

        languageDropdown.setButtonCell(languageDropdown.getCellFactory().call(null));

        languageDropdown.setOnAction(event -> {
            Locale selectedLocale = languageDropdown.getValue();
            Locale.setDefault(selectedLocale);
            StringRes.setRes(selectedLocale);
            try {
                switchScene(AppScene.LOGIN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        footerContainer.getChildren().add(languageDropdown);

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

        exitButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_CLOSE,
            StringRes.get("exit_fab_text"),
            Color.RED,
            Color.WHITE,
            Color.WHITE
        ));

        loginButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_LOGIN,
            StringRes.get("login_label"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(languageDropdown);
        addHoverEffect(exitButton);
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
                FramelessStyledAlert.show(
                    StringRes.get("login_failed_alert_title"),
                    StringRes.get("login_failed_alert_content")
                );
            }
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}