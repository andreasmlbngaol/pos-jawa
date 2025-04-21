package com.jawa.utsposclient.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class JawaButton {
    private static final int BUTTON_SIZE = 30;
    private static final int BORDER_SIZE = 12;
    private static final int ICON_SIZE = BUTTON_SIZE - BORDER_SIZE;

    public static Button createIconButton(
        MaterialDesign iconCode,
        Color backgroundColor,
        Color iconColor
    ) {
        FontIcon icon = new FontIcon(iconCode);
        icon.setIconColor(iconColor);
        icon.setIconSize(ICON_SIZE);

        Button button = new Button();
        button.setGraphic(icon);
        button.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 6;", toCssColor(backgroundColor)));
        button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        return button;
    }

    private static String toCssColor(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255)
        );
    }

    public static Button createExtendedFab(MaterialDesign iconCode, String labelText, Color backgroundColor, Color iconColor, Color textColor) {
        FontIcon icon = new FontIcon(iconCode);
        icon.setIconColor(iconColor);
        icon.setIconSize(ICON_SIZE);

        Label label = new Label(labelText);
        label.setTextFill(textColor);
        label.setStyle("-fx-font-weight: bold;");

        HBox content;
        if(labelText.length() > 1) {
            content = new HBox(8, icon, label);
        } else {
            content = new HBox(icon);
        }
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(content);
        button.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-background-radius: 50;
        """, toCssColor(backgroundColor)));

        return button;
    }
}
