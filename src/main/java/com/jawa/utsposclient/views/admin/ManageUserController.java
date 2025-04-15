package com.jawa.utsposclient.views.admin;


import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.repo.UserRepository;
import com.jawa.utsposclient.utils.Strings;
import com.jawa.utsposclient.views.fragment.AddCashierDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;


public class ManageUserController extends AdminController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Long> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> actionColumn;

    @FXML
    private void initialize() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        ObservableList<User> users = FXCollections.observableArrayList(UserRepository.getAllUsers());
        userTable.setItems(users);
    }

    @FXML
    private void onShowAddCashierDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("views/fragment/add-cashier-dialog-fragment.fxml"));
            loader.setResources(Strings.getBundle());
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Cashier");

            // Show the dialog and wait for the user to click Save or Cancel
            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE ||
                    result.getText().equalsIgnoreCase("Save")) {

                    AddCashierDialogController controller = loader.getController();

                    // Panggil langsung jika butuh melakukan aksi setelah dialog ditutup
                    // atau pastikan aksi dilakukan saat tombol "Save" ditekan di controllernya
                    System.out.println("Cashier added!");
                }
            });
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
