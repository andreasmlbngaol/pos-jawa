package com.jawa.utsposclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(isSessionValid());
//        if(!isSessionValid()) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            stage.setTitle("Login!");
            stage.setScene(scene);
            stage.show();
//        }
//        else {
//            var cookies = ApiClient.getInstance().getCookieManager().getCookieStore().getCookies();
//            for(HttpCookie cookie : cookies) {
//                String name = cookie.getName();
//                String rawValue = cookie.getValue();
//                String decodedValue = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);
//
//                System.out.println(name + "=" + decodedValue);
//                SessionManager.clearSession();
//            }
//        }
    }

    private boolean isSessionValid() {
        List<HttpCookie> cookies = ApiClient.getInstance()
                .getCookieManager()
                .getCookieStore()
                .get(SessionManager.SESSION_URI);

        for (HttpCookie cookie : cookies) {
            if("USER_SESSION".equals(cookie.getName()) && !cookie.hasExpired()) {
                System.out.println(cookie.getValue());
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        launch();
    }
}