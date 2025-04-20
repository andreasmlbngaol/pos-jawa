package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.dto.PurchaseTransaction;
import com.jawa.utsposclient.dto.TransactionItem;
import com.jawa.utsposclient.utils.DateUtils;
import com.jawa.utsposclient.utils.StringUtils;
import com.jawa.utsposclient.views.Controller;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BillController extends Controller {
    @FXML private Label transactionIdLabel;
    @FXML private Label cashierLabel;
    @FXML private Label dateLabel;
    @FXML private TableView<TransactionItem> billTable;
    @FXML private TableColumn<TransactionItem, String> itemNameColumn;
    @FXML private TableColumn<TransactionItem, Integer> itemQuantityColumn;
    @FXML private TableColumn<TransactionItem, String> itemPriceColumn;
    @FXML private Label totalLabel;
    @FXML private Label paidLabel;
    @FXML private Label changeLabel;

    public void setTransaction(PurchaseTransaction transaction) {
        System.out.println(transaction.getId());
        transactionIdLabel.setText("#" + transaction.getId());
        var cashier = transaction.getUser();
        cashierLabel.setText(String.format("%s (%s)", cashier.getName(), cashier.getUsername()));
        dateLabel.setText(DateUtils.formatDateTime(transaction.getCreatedAt()));

        billTable.getItems().setAll(transaction.getItems());
        itemNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getName()));
        itemQuantityColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantity()));
        itemPriceColumn.setCellValueFactory(
            cell -> new SimpleObjectProperty<>(StringUtils.moneyFormat(cell.getValue().getTotalPrice()))
        );

        totalLabel.setText(StringUtils.moneyFormat(transaction.getTotalAmount()));
        paidLabel.setText(StringUtils.moneyFormat(transaction.getPaidAmount()));
        changeLabel.setText(StringUtils.moneyFormat(transaction.getChangeAmount()));
    }

}
