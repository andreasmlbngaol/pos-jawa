package com.jawa.utsposclient;

import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.SessionManager;
import com.jawa.utsposclient.views.Controller;
import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.enums.AppScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            Database.init();


            AppScene firstScene;
            if(SessionManager.loadSession()) {
                var user = JawaAuth.getInstance().getCurrent();
                if(user.isMustChangePassword()) firstScene = AppScene.SET_PASSWORD;
                else if(user.getRole() == Role.Admin) firstScene = AppScene.ADMIN_HOME;
                else firstScene = AppScene.CASHIER_HOME;
            } else firstScene = AppScene.LOGIN;
            // Nanti ini check udah login atau belum. Kalau udah gak perlu ke login lagi. Tapi belakangan

            var controller = new Controller();

            controller.setStage(stage);
            controller.switchScene(firstScene);

            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }
    }
}