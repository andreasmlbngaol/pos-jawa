package com.jawa.utsposclient.views.cashier;

import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.PurchaseTransaction;
import com.jawa.utsposclient.dto.RefundTransaction;
import com.jawa.utsposclient.dto.TransactionItem;
import com.jawa.utsposclient.repo.TransactionRepository;
import com.jawa.utsposclient.utils.DateUtils;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RefundTransactionController extends CashierController {

    @FXML private TextField transactionIdTextField;
    @FXML private TextField cashierNameTextField;
    @FXML private TextField createdAtTextField;

    @FXML private TableView<TransactionItem> originalTable;
    @FXML private TableView<TransactionItem> refundTable;

    private final ObservableList<TransactionItem> originalItems = FXCollections.observableArrayList();
    private final ObservableList<TransactionItem> refundItems = FXCollections.observableArrayList();

    @FXML private TableColumn<TransactionItem, String> skuOriginalColumn;
    @FXML private TableColumn<TransactionItem, String> originalNameColumn;
    @FXML private TableColumn<TransactionItem, Number> originalPriceColumn;
    @FXML private TableColumn<TransactionItem, Number> originalQuantityColumn;
    @FXML private TableColumn<TransactionItem, Number> originalTotalColumn;
    @FXML private TableColumn<TransactionItem, Void> originalActionColumn;

    @FXML private TableColumn<TransactionItem, String> refundSkuColumn;
    @FXML private TableColumn<TransactionItem, String> refundNameColumn;
    @FXML private TableColumn<TransactionItem, Number> refundPriceColumn;
    @FXML private TableColumn<TransactionItem, Number> refundQuantityColumn;
    @FXML private TableColumn<TransactionItem, Number> refundTotalColumn;

    @FXML private Button backButton;
    @FXML private Button executeButton;

    @FXML
    private void initialize() {
        backButton.setGraphic(JawaButton.createExtendedFab(
                MaterialDesign.MDI_ARROW_LEFT,
                "",
                Color.web("#e8b323"),
                Color.WHITE,
                Color.WHITE
        ));

        executeButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_REFRESH,
            StringRes.get("refund_title"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(backButton);
        addHoverEffect(executeButton);
        transactionIdTextField.setOnKeyPressed(event -> {
            if(event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                loadTransactionById();
            }
        });

        skuOriginalColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProduct().getSku()));

        originalNameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProduct().getName()));

        originalPriceColumn.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getPricePerItem()));

        originalQuantityColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getQuantity()));

        originalTotalColumn.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getTotalPrice()));

        originalActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button refundButton = JawaButton.createIconButton(MaterialDesign.MDI_RELOAD, Color.RED, Color.WHITE);

            {
                refundButton.setOnAction(event -> {
                    TransactionItem item = getTableView().getItems().get(getIndex());
                    addItemToRefund(item);
                    updateButtonState();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    TransactionItem currentItem = getTableView().getItems().get(getIndex());
                    if (refundItems.contains(currentItem)) {
                        refundButton.setGraphic(JawaButton.createIconButton(MaterialDesign.MDI_CHECK, Color.RED, Color.WHITE));
                        refundButton.setDisable(true);
                    } else {
                        refundButton.setGraphic(JawaButton.createIconButton(MaterialDesign.MDI_RELOAD, Color.RED, Color.WHITE));
                        refundButton.setDisable(false);
                    }
                    setGraphic(refundButton);
                }
            }

            private void updateButtonState() {
                originalTable.refresh();
            }
        });

        refundSkuColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProduct().getSku()));

        refundNameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProduct().getName()));

        refundPriceColumn.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getPricePerItem()));

        refundQuantityColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getQuantity()));

        refundTotalColumn.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getTotalPrice()));
    }

    @FXML
    private void loadTransactionById() {
        try {
            long transactionId = Long.parseLong(transactionIdTextField.getText().trim());
            PurchaseTransaction trx = TransactionRepository.getPurchaseTransactionById(transactionId);
            boolean exist = TransactionRepository.isRefundTransactionExist(transactionId);
            if(exist) {
                FramelessStyledAlert.show(
                    StringRes.get("already_refunded_alert_title"),
                    StringRes.getFormatted("already_refunded_alert_content",transactionId)
                );
                return;
            }

            if (trx == null) {
                FramelessStyledAlert.show(
                    StringRes.get("not_found_alert_title"),
                    StringRes.getFormatted("transaction_not_found_alert_content", transactionId)
                );
                return;
            }

            cashierNameTextField.setText(trx.getUser().getName());
            createdAtTextField.setText(DateUtils.formatDateTime(trx.getCreatedAt()));

            originalItems.setAll(trx.getItems());
            originalTable.setItems(originalItems);

        } catch (NumberFormatException e) {
            FramelessStyledAlert.show(
                StringRes.get("bad_request_alert_title"),
                StringRes.get("invalid_transaction_id_alert_content")
            );
        }
    }

    /**
     * Tambahkan item ke daftar refund
     */
    public void addItemToRefund(TransactionItem item) {
        if (!refundItems.contains(item)) {
            refundItems.add(item);
            refundTable.setItems(refundItems);
        }
    }

    /**
     * Eksekusi transaksi refund
     */
    @FXML
    private void onExecuteRefund() {
        if (refundItems.isEmpty()) {
            FramelessStyledAlert.show(
                StringRes.get("no_item_alert_title"),
                StringRes.get("no_item_alert_content")
            );
            return;
        }

        try {
            long transactionId = Long.parseLong(transactionIdTextField.getText().trim());
            PurchaseTransaction originalTransaction = TransactionRepository.getPurchaseTransactionById(transactionId);

            if (originalTransaction == null) return;

            boolean confirmed = FramelessStyledAlert.showConfirmation(
                StringRes.get("refund_confirmation_alert_title"),
                StringRes.get("refund_confirmation_alert_content")
            );

            if(!confirmed) return;

            RefundTransaction refundTransaction = new RefundTransaction();
            refundTransaction.setUser(originalTransaction.getUser());
            refundTransaction.setCreatedAt(LocalDateTime.now());

            double total = 0;
            ArrayList<TransactionItem> newItems = new ArrayList<>();

            for (TransactionItem originalItem : refundItems) {
                TransactionItem item = new TransactionItem();
                item.setProduct(originalItem.getProduct());
                item.setPricePerItem(originalItem.getPricePerItem());
                item.setQuantity(originalItem.getQuantity());
                item.setTotalPrice(originalItem.getTotalPrice());
                newItems.add(item);
                total += item.getTotalPrice();
            }

            refundTransaction.setItems(newItems);
            var itemCount = refundTransaction.getItems().size();
            var reason = StringRes.getFormatted("refund_reason", itemCount, itemCount > 1 ? "s" : "" );
            refundTransaction.setRefundReason(reason);
            refundTransaction.setTotalAmount(total);
            refundTransaction.setPurchaseTransaction(originalTransaction);

            var id = refundTransaction.processTransactionAndGetId();
            LogsDao.createRefundTransaction(user.getId(), id);
            FramelessStyledAlert.show(
                StringRes.get("refund_success_alert_title"),
                StringRes.getFormatted("refund_success_alert_content", id)
            );
            resetForm();

        } catch (NumberFormatException e) {
            FramelessStyledAlert.show(
                StringRes.get("bad_request_alert_title"),
                StringRes.get("invalid_transaction_id_alert_content")
            );
        }
    }

    /**
     * Reset semua field
     */
    private void resetForm() {
        transactionIdTextField.clear();
        cashierNameTextField.clear();
        createdAtTextField.clear();
        originalItems.clear();
        refundItems.clear();
        originalTable.getItems().clear();
        refundTable.getItems().clear();
    }
}
