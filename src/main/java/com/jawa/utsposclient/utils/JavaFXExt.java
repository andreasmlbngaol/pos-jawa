package com.jawa.utsposclient.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

public class JavaFXExt {
    public static void showCopyableAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Supaya ukurannya gak mepet
        textArea.setMinHeight(Region.USE_PREF_SIZE);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }
}
