package com.jawa.utsposclient.controller;

import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.utils.AppScene;
import com.jawa.utsposclient.utils.Strings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Switch to a new scene using the given AppScene.
     * Used by MainApp and other controllers.
     */
    public void switchScene(AppScene scenePair) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(scenePair.getFxml()));
        fxmlLoader.setResources(Strings.getBundle());
        Scene scene = new Scene(fxmlLoader.load(), AppScene.width, AppScene.height);

        Object controller = fxmlLoader.getController();
        if (controller instanceof Controller) {
            ((Controller) controller).setStage(stage);
        }

        stage.setScene(scene);
        stage.setTitle(scenePair.getTitle());
    }
}
