package com.jawa.utsposclient.views.admin;


import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dao.LogsDao;
import com.jawa.utsposclient.dto.*;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.enums.ProductType;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.utils.FramelessStyledAlert;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import com.jawa.utsposclient.utils.StringUtils;
import com.jawa.utsposclient.views.fragment.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

@SuppressWarnings("unused")
public class ManageProductController extends AdminController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Long> productIdColumn;
    @FXML private TableColumn<Product, String> skuColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> typeColumn;
    @FXML private TableColumn<Product, Void> actionColumn;

    @FXML private Button backButton;
    @FXML private Button addProductButton;

    @FXML private TextField searchField;


    private final ObservableList<Product> masterProductList = FXCollections.observableArrayList();
    private FilteredList<Product> filteredProductList;

    private void loadProducts() {
        masterProductList.setAll(ProductRepository.getAllProducts());

        if (filteredProductList == null) {
            filteredProductList = new FilteredList<>(masterProductList, p -> true);
            productTable.setItems(filteredProductList);
        }
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

        addProductButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_DATABASE_PLUS,
            StringRes.get("add_product_label"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(backButton);
        addHoverEffect(addProductButton);

        productTable.setEditable(false);

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(StringUtils.moneyFormat(item));
                }

                addHoverEffect(backButton);
                addHoverEffect(addProductButton);
            }
        });

        typeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getType().getDisplayName())
        );

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = JawaButton.createIconButton(MaterialDesign.MDI_PENCIL, Color.LIMEGREEN, Color.BLACK);
            private final Button deleteButton = JawaButton.createIconButton(MaterialDesign.MDI_DELETE, Color.RED, Color.WHITE);
            private final HBox container = new HBox(10, editButton, deleteButton);

            {
                editButton.setTooltip(new Tooltip(StringRes.get("edit_product_tooltip")));
                deleteButton.setTooltip(new Tooltip(StringRes.get("delete_product_tooltip")));

                editButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    ProductType type = product.getType();

                    switch (type) {
                        case Perishable -> showEditDialog(
                            (PerishableProduct) product,
                            AppScene.EDIT_PERISHABLE_DIALOG,
                            EditPerishableDialogController.class,
                            (p, c) -> c.setProduct(p)
                        );
                        case NonPerishable -> showEditDialog(
                            (NonPerishableProduct) product,
                            AppScene.EDIT_NON_PERISHABLE_DIALOG,
                            EditNonPerishableDialogController.class,
                            (p, c) -> c.setProduct(p)
                        );
                        case Digital -> showEditDialog(
                            (DigitalProduct) product,
                            AppScene.EDIT_DIGITAL_DIALOG,
                            EditDigitalDialogController.class,
                            (p, c) -> c.setProduct(p)
                        );
                        case Bundle -> showEditBundleDialog(product);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    boolean confirmed = FramelessStyledAlert.showConfirmation(
                        StringRes.get("delete_product_alert_title"),
                        StringRes.getFormatted("delete_product_alert_content", product.getName())
                    );

                    if (confirmed) {
                        ProductRepository.softDelete(product.getId());
                        LogsDao.deleteProduct(user.getId(), product.getId());
                        FramelessStyledAlert.show(
                            StringRes.get("delete_success_alert_title"),
                            StringRes.getFormatted("delete_product_success_alert_content", product.getSku(), product.getName()));
                        loadProducts();
                    }
                });

                container.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        loadProducts();
        searchField.textProperty().addListener((obs, oldValue, newValue) -> filteredProductList.setPredicate(product -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            return product.getName().toLowerCase().contains(lowerCaseFilter) ||
                    product.getSku().toLowerCase().contains(lowerCaseFilter);
        }));


    }

    @FXML
    private void onShowAddProductDialog() {
        try {
            AppScene dialogScene = AppScene.ADD_PRODUCT_DIALOG;
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(dialogScene.getFxml()));
            loader.setResources(StringRes.getBundle());
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(dialogScene.getTitle());

            dialog.showAndWait().ifPresent(result -> {
                if(result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    var id = ((AddProductDialogController) loader.getController()).onAddProductAndGetId();
                    LogsDao.addProduct(user.getId(), id);

                    FramelessStyledAlert.show(
                        StringRes.get("add_product_success_alert_title"),
                        StringRes.getFormatted("add_product_success_alert_content", id)
                    );
                }
            });

            loadProducts();
        } catch (Exception e) {
            FramelessStyledAlert.show(
                StringRes.get("bad_request_alert_title"),
                StringRes.get("add_product_failed_alert_content")
            );
            System.err.println(e.getMessage());
        }
    }

    private void showEditBundleDialog(Product product) {
        var bundle = (BundleProduct) product;
    }

    private <T extends Product, C> void showEditDialog(
        T product,
        AppScene dialogScene,
        Class<C> controllerClass,
        DialogControllerInitializer<T, C> initializer
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(dialogScene.getFxml()));
            loader.setResources(StringRes.getBundle());
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(dialogScene.getTitle());

            C controller = loader.getController();
            initializer.init(product, controller);

            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    if (controller instanceof ProductDialogController pdc) {
                        pdc.onUpdateProduct();
                    }
                    LogsDao.updateProduct(user.getId(), product.getId());

                    FramelessStyledAlert.show(
                        StringRes.get("update_product_success_alert_title"),
                        StringRes.getFormatted("sku_name_format", product.getSku(), product.getName())
                    );
                    loadProducts();
                }
            });
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @FunctionalInterface
    private interface DialogControllerInitializer<T, C> {
        void init(T product, C controller);
    }

}

