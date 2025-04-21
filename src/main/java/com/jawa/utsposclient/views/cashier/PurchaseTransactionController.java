package com.jawa.utsposclient.views.cashier;

import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.PurchaseTransaction;
import com.jawa.utsposclient.dto.TransactionItem;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import com.jawa.utsposclient.utils.StringUtils;
import com.jawa.utsposclient.views.fragment.BillController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;
import java.time.LocalDateTime;

public class PurchaseTransactionController extends CashierController {

    @FXML private TextField skuTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField totalPriceTextField;
    @FXML private TextField paidTextField;

    @FXML private TableView<TransactionItem> productTable;
    @FXML private TableColumn<TransactionItem, String> skuColumn;
    @FXML private TableColumn<TransactionItem, String> nameColumn;
    @FXML private TableColumn<TransactionItem, Double> totalItemPriceColumn;
    @FXML private TableColumn<TransactionItem, Integer> quantityColumn;

    @FXML private Button backButton;
    @FXML private Button chargeButton;

    private double getGrandTotal() {
        return productTable.getItems().stream()
            .mapToDouble(item -> item != null && item.getTotalPrice() != null ? item.getTotalPrice() : 0)
            .sum();
    }

    private void updateTableAndTotal() {
        productTable.refresh();
        totalPriceTextField.setText(StringUtils.moneyFormat(getGrandTotal()));
    }

    @FXML
    private void initialize() {

        backButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_ARROW_LEFT,
            "",
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));
        chargeButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_CONTENT_SAVE_ALL,
            StringRes.get("checkout_label"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE

        ));

        addHoverEffect(backButton);
        addHoverEffect(chargeButton);


        productTable.setEditable(true);
        skuColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getProduct().getSku())
        );
        nameColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getProduct().getName())
        );
        quantityColumn.setEditable(true);
        quantityColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getQuantity())
        );
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            TransactionItem item = event.getRowValue();
            Integer newValue = event.getNewValue();

            if (newValue != null && newValue > 0) item.setQuantity(newValue);
            else item.setQuantity(event.getOldValue());
            updateTableAndTotal();

        });

        totalItemPriceColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getTotalPrice())
        );

        totalItemPriceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(StringUtils.moneyFormat(item));
                }

            }
        });

        totalPriceTextField.setEditable(false);
        totalPriceTextField.setText(StringUtils.moneyFormat(0.0));


        skuTextField.setOnKeyPressed(event -> {
            if(event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                var sku = skuTextField.getText().toUpperCase();
                var product = ProductRepository.getProductBySku(sku);
                if(product != null) {
                    nameTextField.setText(product.getName());
                    priceTextField.setText(StringUtils.moneyFormat(product.getPrice()));

                    var items = productTable.getItems();
                    boolean found = false;
                    for(var item : items) {
                        if(item.getProduct().getSku().equals(product.getSku())) {
                            item.incrementQuantity();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        items.add(new TransactionItem(product));
                    }

                    updateTableAndTotal();
                    skuTextField.clear();
                } else {
                    FramelessStyledAlert.show(
                        StringRes.get("not_found_alert_title"),
                        StringRes.get("product_not_found_alert_content")
                    );
                    nameTextField.clear();
                    priceTextField.clear();
                    skuTextField.requestFocus();
                }
            }
        });

        paidTextField.setOnKeyPressed(event -> {
            if(event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                onExecuteTransaction();
            }
        });
    }

    @FXML
    private void onExecuteTransaction() {
        var total = getGrandTotal();
        if(total <= 0) {
            FramelessStyledAlert.show(
                StringRes.get("empty_chart_alert_title"),
                StringRes.get("empty_chart_alert_content")
            );
            return;
        }

        try {
            var paid = Double.parseDouble(paidTextField.getText());

            var change = paid - total;
            if (change < 0) {
                FramelessStyledAlert.show(
                    StringRes.get("invalid_paid_alert_title") ,
                    StringRes.get("less_paid_alert_content")
                );
            } else {
                var confirmed = FramelessStyledAlert.showConfirmation(
                    StringRes.get("confirm_process_transaction_alert_title"),
                    StringRes.get("confirm_process_transaction_alert_content")
                );
                if (!confirmed) return;

                var transaction = new PurchaseTransaction();
                transaction.setUser(user);
                transaction.setTotalAmount(total);
                transaction.setPaidAmount(paid);
                transaction.setChangeAmount(change);
                transaction.setCreatedAt(LocalDateTime.now());
                transaction.setItems(productTable.getItems());

                var id = transaction.processTransactionAndGetId();
                transaction.setId(id);

                var bill = AppScene.PURCHASE_BILL;
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(bill.getFxml()));
                try {
                    Parent root = loader.load();
                    BillController billController = loader.getController();
                    billController.setTransaction(transaction);

                    Stage stage = new Stage();
                    stage.setTitle(bill.getTitle());
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                LogsDao.createPurchaseTransaction(user.getId(), id);
                resetPurchase(); // ← Panggil setelah berhasil

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            FramelessStyledAlert.show(
                StringRes.get("invalid_paid_alert_title"),
                StringRes.get("invalid_paid_alert_content")
            );
        }

    }

    private void resetPurchase() {
        skuTextField.clear();
        nameTextField.clear();
        priceTextField.clear();
        paidTextField.clear();
        totalPriceTextField.setText(StringUtils.moneyFormat(0.0));
        productTable.getItems().clear();
    }
}
