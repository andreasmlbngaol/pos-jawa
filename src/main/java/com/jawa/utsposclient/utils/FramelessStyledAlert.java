package com.jawa.utsposclient.utils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FramelessStyledAlert {

    public static void show(String title, String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED); // <- No OS border
        dialog.initStyle(StageStyle.TRANSPARENT); // full transparency, no borders

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", 16));
        titleLabel.setTextFill(Color.WHITE);

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("System", 13));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        okButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(10, titleLabel, messageLabel, okButton);
        layout.setStyle("""
            -fx-background-color: rgba(30,30,30,0.95);
            -fx-background-radius: 12;
            -fx-padding: 20;
        """);
        layout.setAlignment(Pos.CENTER);
        layout.setMinWidth(300);

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT); // make whole scene transparent
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static void showCopyable(String title, String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT); // fully borderless and transparent

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", 16));
        titleLabel.setTextFill(Color.WHITE);

        TextArea messageArea = new TextArea(message);
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setFont(Font.font("System", 13));
        messageArea.setStyle("""
    -fx-control-inner-background: rgba(30,30,30,0.95);
    -fx-background-color: rgba(30,30,30,0.95);
    -fx-text-fill: white;
    -fx-highlight-fill: #4a90e2;
    -fx-highlight-text-fill: white;
    -fx-border-color: transparent;
    -fx-focus-color: transparent;
    -fx-faint-focus-color: transparent;
    -fx-padding: 0;
""");
        messageArea.setFocusTraversable(false);
        messageArea.setMouseTransparent(false);

// 🔥 Auto-height sesuai jumlah baris teks
        int lineCount = message.split("\n").length + message.length() / 40; // perkiraan line
        messageArea.setPrefRowCount(Math.min(lineCount, 10)); // max 10 rows
        messageArea.setMaxHeight(Region.USE_PREF_SIZE);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        okButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(10, titleLabel, messageArea, okButton);
        layout.setStyle("""
            -fx-background-color: rgba(30,30,30,0.95);
            -fx-background-radius: 12;
            -fx-padding: 20;
        """);
        layout.setAlignment(Pos.CENTER);
        layout.setMinWidth(300);

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static boolean showConfirmation(String title, String message) {
        final boolean[] confirmed = {false};

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", 16));
        titleLabel.setTextFill(Color.WHITE);

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("System", 13));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);

        Button yesButton = new Button("Yes");
        yesButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        yesButton.setOnAction(e -> {
            confirmed[0] = true;
            dialog.close();
        });

        Button noButton = new Button("Cancel");
        noButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        noButton.setOnAction(e -> dialog.close());

        // 🔥 Tombol horizontal
        HBox buttonBox = new HBox(10, yesButton, noButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, titleLabel, messageLabel, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("""
        -fx-background-color: rgba(30,30,30,0.95);
        -fx-background-radius: 12;
        -fx-padding: 20;
    """);
        layout.setMinWidth(300);

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();

        return confirmed[0];
    }
}

