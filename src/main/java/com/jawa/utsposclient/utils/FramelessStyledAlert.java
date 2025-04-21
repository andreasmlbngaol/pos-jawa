package com.jawa.utsposclient.utils;

import com.jawa.utsposclient.dto.BundleItem;
import com.jawa.utsposclient.dto.Product;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;

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

        Button yesButton = new Button(StringRes.get("yes_label"));
        yesButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        yesButton.setOnAction(e -> {
            confirmed[0] = true;
            dialog.close();
        });

        Button noButton = new Button(StringRes.get("cancel_label"));
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

    public static Optional<BundleItem> showProductChoiceWithQuantity(List<Product> products) {
        final BundleItem[] result = {null};

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        Label titleLabel = new Label(StringRes.get("add_bundle_product_title"));
        titleLabel.setFont(Font.font("System", 16));
        titleLabel.setTextFill(Color.WHITE);

        TextField searchField = new TextField();
        searchField.setPromptText(StringRes.get("add_bundle_product_search_prompt"));
        searchField.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;");
        searchField.setPrefWidth(250);

        ListView<Product> listView = new ListView<>();
        listView.getItems().addAll(products);
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : StringRes.getFormatted("sku_name_format", item.getSku(), item.getName()));
            }
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            listView.getItems().setAll(
                products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lower))
                    .toList()
            );
        });

        TextField quantityField = new TextField("1");
        quantityField.setPromptText(StringRes.get("quantity_label"));
        quantityField.setPrefWidth(80);
        quantityField.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;");

        var qtyLabel = new Label(StringRes.get("quantity_label"));
        qtyLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        HBox qtyBox = new HBox(10, qtyLabel, quantityField);
        qtyBox.setAlignment(Pos.CENTER);


        Button addButton = new Button(StringRes.get("add_label"));
        addButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");

        Button cancelButton = new Button(StringRes.get("cancel_label"));
        cancelButton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");

        HBox buttonBox = new HBox(10, addButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        addButton.setOnAction(e -> {
            Product selected = listView.getSelectionModel().getSelectedItem();
            String qtyStr = quantityField.getText();

            try {
                int qty = Integer.parseInt(qtyStr);
                if (qty <= 0) throw new NumberFormatException();

                if (selected != null) {
                    result[0] = new BundleItem(selected, qty);
                    dialog.close();
                }
            } catch (NumberFormatException ex) {
                show(
                    StringRes.get("error_add_item_alert_title"),
                    StringRes.get("error_add_item_alert_content")
                );
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(10,
            titleLabel,
            searchField,
            listView,
            qtyBox,
            buttonBox
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("""
        -fx-background-color: rgba(30,30,30,0.95);
        -fx-background-radius: 12;
        -fx-padding: 20;
    """);
        layout.setMinWidth(350);
        layout.setPrefHeight(400);

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();

        return Optional.ofNullable(result[0]);
    }
}

