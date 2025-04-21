package com.jawa.utsposclient.views.admin;


import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.dto.User;
import com.jawa.utsposclient.enums.Role;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.JawaAuth;
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
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;


public class ManageUserController extends AdminController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Long> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Void> actionColumn;

    @FXML private Button backButton;
    @FXML private Button addCashierButton;

    private final Admin admin = (Admin) JawaAuth.getInstance().getCurrent();

    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(admin.getAllUsers());
        userTable.setItems(users);
    }

    @FXML
    private void initialize() {

        addHoverEffect(backButton);
        addHoverEffect(addCashierButton);

        backButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_ARROW_LEFT,
            "",
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addCashierButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_ACCOUNT_PLUS,
            StringRes.get("show_add_cashier_dialog_button"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        userTable.setEditable(true);

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setEditable(true);
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button resetPasswordButton = JawaButton.createIconButton(MaterialDesign.MDI_RELOAD, Color.GOLD, Color.BLACK);
            private final Button deleteButton = JawaButton.createIconButton(MaterialDesign.MDI_DELETE, Color.RED, Color.WHITE);;
            private final HBox container = new HBox(10, resetPasswordButton, deleteButton);

            {
                resetPasswordButton.setTooltip(new Tooltip("Reset Password"));
                deleteButton.setTooltip(new Tooltip("Delete User"));

                resetPasswordButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    if(user.getRole() == Role.Cashier) {
                        LogsDao.resetPassword(admin.getId(), user.getId());
                        String otp = admin.resetPasswordAndGetOtp(user.getId());
                        FramelessStyledAlert.showCopyable("OTP", String.format("OTP: %s", otp));
                    }
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());

                    if(user.getRole() == Role.Cashier) {
                        LogsDao.deleteCashier(admin.getId(), user.getId());
                        boolean confirmed = FramelessStyledAlert.showConfirmation(
                            "Delete?",
                            "Are you sure you want to delete this user?"
                        );

                        if (confirmed) {
                            admin.softDelete(user.getId());
                            loadUsers();
                        }
                    }
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
                LogsDao.updateCashierName(admin.getId(), user.getId(), newName.trim());
                admin.changeName(user.getId(), newName.trim());
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
            dialog.setTitle(appScene.getTitle());

            // Show the dialog and wait for the user to click Save or Cancel
            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    var otp = ((AddCashierDialogController) loader.getController()).onAddCashierAndGetOtp();
                    LogsDao.addCashier(admin.getId());

                    FramelessStyledAlert.showCopyable("OTP", String.format("OTP: %s", otp));
                }
            });

            loadUsers();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
