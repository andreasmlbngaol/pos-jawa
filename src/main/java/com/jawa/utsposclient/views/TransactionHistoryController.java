package com.jawa.utsposclient.views;

import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dto.PurchaseTransaction;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.TransactionRepository;
import com.jawa.utsposclient.utils.DateUtils;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import com.jawa.utsposclient.utils.StringUtils;
import com.jawa.utsposclient.views.fragment.BillController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;

public class TransactionHistoryController extends Controller {
    @FXML private Button backButton;

    @FXML private TableView<PurchaseTransaction> transactionTable;
    @FXML private TableColumn<PurchaseTransaction, Long> transactionIdColumn;
    @FXML private TableColumn<PurchaseTransaction, String> createdAtColumn;
    @FXML private TableColumn<PurchaseTransaction, Long> cashierIdColumn;
    @FXML private TableColumn<PurchaseTransaction, String> cashierUsernameColumn;
    @FXML private TableColumn<PurchaseTransaction, String> cashierNameColumn;
    @FXML private TableColumn<PurchaseTransaction, String> totalColumn;
    @FXML private TableColumn<PurchaseTransaction, Void> actionColumn;

    @FXML private TextField searchTextField;
    private ObservableList<PurchaseTransaction> masterTransactions;



    @FXML private void onBackToHome() throws IOException {
        if(user.getRole() == Role.Admin) switchScene(AppScene.ADMIN_HOME);
        else switchScene(AppScene.CASHIER_HOME);
    }

    private void loadTransactions() {
        masterTransactions = FXCollections.observableArrayList(TransactionRepository.getAllPurchaseTransactions());
        transactionTable.setItems(masterTransactions);
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

        addHoverEffect(backButton);

        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        createdAtColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(DateUtils.formatDateTime(cellData.getValue().getCreatedAt()))
        );
        cashierIdColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getUser().getId())
        );
        cashierUsernameColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getUser().getUsername())
        );
        cashierNameColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getUser().getName())
        );
        totalColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(StringUtils.moneyFormat(cellData.getValue().getTotalAmount()))
        );

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button showReceiptButton = JawaButton.createIconButton(
                MaterialDesign.MDI_FILE_DOCUMENT,
                Color.LIMEGREEN,
                Color.BLACK
            );
            private final HBox container = new HBox(showReceiptButton);


            {
                showReceiptButton.setTooltip(new Tooltip(StringRes.get("show_receipt_tooltip")));
                showReceiptButton.setOnAction(event -> {
                    var bill = AppScene.PURCHASE_BILL;
                    FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(bill.getFxml()));
                    try {
                        Parent root = loader.load();
                        BillController billController = loader.getController();
                        billController.setTransaction(col.getTableView().getItems().get(getIndex()));

                        Stage stage = new Stage();
                        stage.setTitle(bill.getTitle());
                        stage.setScene(new Scene(root));
                        stage.show();

                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
                container.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty) {
                    setGraphic(container);
                } else {
                    setGraphic(null);
                }
            }

        });

        loadTransactions();
        FilteredList<PurchaseTransaction> filteredList = new FilteredList<>(masterTransactions, p -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(transaction -> {
            if (newValue == null || newValue.isEmpty()) return true;

            String lowerCaseFilter = newValue.toLowerCase();

            return String.valueOf(transaction.getId()).toLowerCase().contains(lowerCaseFilter);
        }));

        transactionTable.setItems(filteredList);

    }
}
