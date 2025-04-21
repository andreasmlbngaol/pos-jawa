package com.jawa.utsposclient.views;

import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.StringRes;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {
    protected User user = JawaAuth.getInstance().getCurrent();

    private Stage stage;

    protected void  addHoverEffect(Node node) {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        shadow.setColor(Color.web("#ffffff", 0.4));

        node.setOnMouseEntered(e -> {
            node.setEffect(shadow);

            ScaleTransition st = new ScaleTransition(
                    Duration.millis(200), node
            );
            st.setToX(1.10);
            st.setToY(1.10);
            st.play();
        });

        node.setOnMouseExited(e -> {
            node.setEffect(null);

            ScaleTransition st = new ScaleTransition(
                    Duration.millis(200), node
            );
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

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
            StringRes.get("logout_alert_title"),
            StringRes.get("logout_alert_content")
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

    @FXML
    protected void onExitApplication() {
        boolean confirmed = FramelessStyledAlert.showConfirmation(
            StringRes.get("exit_app_alert_title"),
            StringRes.get("exit_app_alert_content")
        );

        if (confirmed) {
            System.exit(0);
        }
    }
}
