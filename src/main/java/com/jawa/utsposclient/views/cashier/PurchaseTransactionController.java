package com.jawa.utsposclient.views.cashier;

import com.jawa.utsposclient.dto.PurchaseTransaction;
import com.jawa.utsposclient.dto.TransactionItem;
import com.jawa.utsposclient.entities.PurchaseTransactions;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.repo.TransactionRepository;
import com.jawa.utsposclient.utils.DateUtils;
import com.jawa.utsposclient.utils.StringUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;

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
    @FXML private TableColumn<TransactionItem, Void> actionColumn;


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
            updateTableAndTotal(); // untuk update totalPrice

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
        if(total <= 0) return;

        Alert alert;
        try {
            var paid = Double.parseDouble(paidTextField.getText());

            var change = paid - total;
            if (change < 0) {
                alert = new Alert(Alert.AlertType.ERROR, "Paid is less than total price!");
            } else {
                var transaction = new PurchaseTransaction();
                transaction.setUser(user);
                transaction.setTotalAmount(total);
                transaction.setPaidAmount(paid);
                transaction.setChangeAmount(change);
                transaction.setCreatedAt(DateUtils.localDateToDate(LocalDate.now()));
                transaction.setItems(productTable.getItems());

                TransactionRepository.executePurchaseTransaction(transaction);

                alert = new Alert(Alert.AlertType.INFORMATION, "Change: " + StringUtils.moneyFormat(change));
                resetPurchase(); // ← Panggil setelah berhasil


            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            alert = new Alert(Alert.AlertType.ERROR, "Insert valid paid nominal!");
        }

        alert.showAndWait();
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
