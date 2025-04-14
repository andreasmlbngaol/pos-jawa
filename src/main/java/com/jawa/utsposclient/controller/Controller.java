package com.jawa.utsposclient.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
//    protected final ApiClient apiClient = ApiClient.getInstance();
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void switchScene(String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jawa/utsposclient/views" + fxmlPath));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);

        Object controller = fxmlLoader.getController();
        if (controller instanceof Controller) {
            ((Controller) controller).setStage(stage);
        }

        stage.setScene(scene);
        stage.setTitle(title);
    }
}
