package com.jawa.utsposclient;

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
        loadSession();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();
    }

    private void loadSession() {
        ApiClient.getInstance()
                .getCookieManager()
                .getCookieStore()
                .get(SessionManager.SESSION_URI);
    }

    @SuppressWarnings("unused")
    private boolean isSessionValid() {
        List<HttpCookie> cookies = ApiClient.getInstance()
                .getCookieManager()
                .getCookieStore()
                .get(SessionManager.SESSION_URI);

        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals(ApiClient.SESSION_NAME) && !cookie.hasExpired()) {
                return true;
            }
        }

        return false;
    }
}