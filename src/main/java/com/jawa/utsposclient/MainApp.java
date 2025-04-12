package com.jawa.utsposclient;

import com.jawa.utsposclient.api.ApiClient;
import com.jawa.utsposclient.api.SessionManager;
import com.jawa.utsposclient.controller.Controller;
import com.jawa.utsposclient.enums.Role;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        String loaderPath;
        String title;

        if(!isSessionValid()) {
            loaderPath = "login-view.fxml";
            title = "Login";
        } else {
            var role = SessionManager.getSessionRole();
            if(role == Role.ADMIN) {
                loaderPath = "admin-view.fxml";
                title = "Admin View";
            } else {
                loaderPath = "cashier-view.fxml";
                title = "Cashier View";
            }
        }

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(loaderPath));
        Scene scene = new Scene(loader.load(), 1280, 720);

        Object controller = loader.getController();
        if (controller instanceof Controller baseController) {
            baseController.setStage(stage);
        }

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private boolean isSessionValid() {
        ApiClient client = ApiClient.getInstance();
        List<HttpCookie> cookies = client
                .getCookieManager()
                .getCookieStore()
                .get(SessionManager.SESSION_URI);

        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals(ApiClient.SESSION_NAME) && !cookie.hasExpired()) {
                return true;
            }
        }
        SessionManager.clearLocalSession(client.getCookieManager());
        return false;
    }
}