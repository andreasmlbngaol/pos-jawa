package com.jawa.utsposclient;

import com.jawa.utsposclient.db.Database;
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
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database initialization failed");
            alert.setContentText(e.getMessage());
            System.out.println(e.getMessage());

            alert.showAndWait();
            Platform.exit();
        }
    }
//    @Override
//    public void start(Stage stage) throws IOException {
//        String loaderPath;
//        String title;
//
//        if(!isSessionValid()) {
//            loaderPath = "login-view.fxml";
//            title = "Login";
//        } else {
//            var role = SessionManager.getSessionRole();
//            if(role == Role.ADMIN) {
//                loaderPath = "admin-view.fxml";
//                title = "Admin View";
//            } else {
//                loaderPath = "cashier-view.fxml";
//                title = "Cashier View";
//            }
//        }
//
//        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(loaderPath));
//        Scene scene = new Scene(loader.load(), 1280, 720);
//
//        Object controller = loader.getController();
//        if (controller instanceof Controller baseController) {
//            baseController.setStage(stage);
//        }
//
//        stage.setTitle(title);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    private boolean isSessionValid() {
//        ApiClient client = ApiClient.getInstance();
//        List<HttpCookie> cookies = client
//                .getCookieManager()
//                .getCookieStore()
//                .get(SessionManager.SESSION_URI);
//
//        for (HttpCookie cookie : cookies) {
//            if (cookie.getName().equals(ApiClient.SESSION_NAME) && !cookie.hasExpired()) {
//                return true;
//            }
//        }
//        SessionManager.clearLocalSession(client.getCookieManager());
//        return false;
//    }
}