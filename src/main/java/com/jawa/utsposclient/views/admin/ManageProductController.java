package com.jawa.utsposclient.views.admin;


import com.jawa.utsposclient.MainApp;
import com.jawa.utsposclient.dto.Product;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.utils.StringRes;
import com.jawa.utsposclient.views.fragment.AddProductDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageProductController extends AdminController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Long> productIdColumn;
    @FXML private TableColumn<Product, String> skuColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Void> actionColumn;

    private void loadProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList(ProductRepository.getAllProducts());
        productTable.setItems(products);
    }

    @FXML
    private void initialize() {
        productTable.setEditable(true);

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadProducts();
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
                    ((AddProductDialogController) loader.getController()).onAddProduct();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product added!");
                    alert.showAndWait();
                    System.out.println("Product added!");
                }
            });

            loadProducts();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
