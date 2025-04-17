package com.jawa.utsposclient.views.admin;


import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.repo.UserRepository;
import com.jawa.utsposclient.utils.AppScene;
import com.jawa.utsposclient.utils.StringRes;
import com.jawa.utsposclient.views.fragment.AddCashierDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import java.io.IOException;


public class ManageUserController extends AdminController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Long> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Void> actionColumn;

    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(UserRepository.getAllUsers());
        userTable.setItems(users);
    }

    @FXML
    private void initialize() {
        userTable.setEditable(true);

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setEditable(true);
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button resetPasswordButton = new Button("Reset Password");
            private final Button deleteButton = new Button("Delete");
            private final HBox container = new HBox(10, resetPasswordButton, deleteButton);

            {
                resetPasswordButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());

                    String otp = UserRepository.resetPassword(user.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, otp);
                    alert.showAndWait();
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            UserRepository.softDelete(user.getId());
                            loadUsers();
                        }
                    });
                });

                container.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty) {
                    User user = getTableView().getItems().get(getIndex());
                    if(user.getRole() == Role.Cashier) {
                        setGraphic(container);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });

        nameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String newName = event.getNewValue();

            if(newName != null && !newName.trim().isEmpty()) {
                UserRepository.updateName(user.getId(), newName);
                loadUsers();
            }
        });

        loadUsers();
    }

    @FXML
    private void onShowAddCashierDialog() {
        try {
            AppScene appScene = AppScene.ADD_CASHIER_DIALOG;

            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(appScene.getFxml()));
            loader.setResources(StringRes.getBundle());
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(AppScene.ADD_CASHIER_DIALOG.getTitle());

            // Show the dialog and wait for the user to click Save or Cancel
            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {

                    var otp = ((AddCashierDialogController) loader.getController()).onAddCashierAndGetOtp();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "OTP: " + otp);
                    alert.setTitle("OTP");
                    alert.showAndWait();
                    // Panggil langsung jika butuh melakukan aksi setelah dialog ditutup
                    // atau pastikan aksi dilakukan saat tombol "Save" ditekan di controllernya
                    System.out.println("Cashier added!");
                }
            });

            loadUsers();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
