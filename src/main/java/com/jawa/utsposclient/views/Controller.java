package com.jawa.utsposclient.views;

import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.StringRes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    protected User user = JawaAuth.getInstance().getCurrent();

    private Stage stage;

    public void setStage(Stage stage) {
        stage.setResizable(false);
        this.stage = stage;
    }

    /**
     * Switch to a new scene using the given AppScene.
     * Used by MainApp and other controllers.
     */
    public void switchScene(AppScene appScene) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(appScene.getFxml()));
        fxmlLoader.setResources(StringRes.getBundle());
        Scene scene = new Scene(fxmlLoader.load(), AppScene.width, AppScene.height);
        Object controller = fxmlLoader.getController();
        if (controller instanceof Controller) {
            ((Controller) controller).setStage(stage);
        }

        stage.setScene(scene);
        stage.setTitle(appScene.getTitle());
    }

    @FXML
    protected void onLogout() {
        boolean confirmed = FramelessStyledAlert.showConfirmation(
            StringRes.get("signout_alert_title"),
            "Yakin ingin sign out?\nAksi ini akan mengeluarkan kamu dari sesi saat ini."
        );

        if (confirmed) {
            LogsDao.logout(user.getId());
            user.logout();

            try {
                switchScene(AppScene.LOGIN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
