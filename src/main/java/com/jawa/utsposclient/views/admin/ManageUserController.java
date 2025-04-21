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
import javafx.util.converter.DefaultStringConverter;
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

    @FXML private TextField searchUserField;
    private ObservableList<User> allUsers;


    private final Admin admin = (Admin) JawaAuth.getInstance().getCurrent();

    private void loadUsers() {
        allUsers = FXCollections.observableArrayList(admin.getAllUsers());
        userTable.setItems(allUsers);
    }

    private void filterUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            userTable.setItems(allUsers);
            return;
        }

        String lowerKeyword = keyword.toLowerCase();
        ObservableList<User> filtered = FXCollections.observableArrayList();

        for (User user : allUsers) {
            if (user.getName().toLowerCase().contains(lowerKeyword)) {
                filtered.add(user);
            }
        }

        userTable.setItems(filtered);
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

        addCashierButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_ACCOUNT_PLUS,
            StringRes.get("add_cashier_label"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(backButton);
        addHoverEffect(addCashierButton);

        userTable.setEditable(true);

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(column -> {
            TextFieldTableCell<User, String> cell = new TextFieldTableCell<>(new DefaultStringConverter());
            Tooltip tooltip = new Tooltip("Double click to edit");
            cell.setTooltip(tooltip);
            return cell;
        });
        nameColumn.setEditable(true);
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button resetPasswordButton = JawaButton.createIconButton(MaterialDesign.MDI_RELOAD, Color.GOLD, Color.BLACK);
            private final Button deleteButton = JawaButton.createIconButton(MaterialDesign.MDI_DELETE, Color.RED, Color.WHITE);
            private final HBox container = new HBox(10, resetPasswordButton, deleteButton);

            {
                resetPasswordButton.setTooltip(new Tooltip(StringRes.get("reset_password_tooltip")));
                deleteButton.setTooltip(new Tooltip(StringRes.get("delete_user_tooltip")));

                resetPasswordButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    if(user.getRole() == Role.Cashier) {
                        LogsDao.resetPassword(admin.getId(), user.getId());
                        String otp = admin.resetPasswordAndGetOtp(user.getId());
                        FramelessStyledAlert.showCopyable(
                            StringRes.get("otp_alert_title"),
                            StringRes.getFormatted("otp_alert_content", otp)
                        );
                    }
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());

                    if(user.getRole() == Role.Cashier) {
                        LogsDao.deleteCashier(admin.getId(), user.getId());
                        boolean confirmed = FramelessStyledAlert.showConfirmation(
                            StringRes.get("delete_user_alert_title"),
                            StringRes.getFormatted("delete_user_alert_content", user.getName())
                        );

                        if (confirmed) {
                            admin.softDelete(user.getId());
                            FramelessStyledAlert.show(
                                StringRes.get("delete_success_alert_title"),
                                StringRes.getFormatted("delete_user_success_alert_content", user.getName())
                            );
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
        searchUserField.textProperty().addListener((obs, oldVal, newVal) -> filterUsers(newVal));
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

                    FramelessStyledAlert.showCopyable(
                        StringRes.get("otp_alert_title"),
                        StringRes.getFormatted("otp_alert_content", otp)
                    );
                }
            });

            loadUsers();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
