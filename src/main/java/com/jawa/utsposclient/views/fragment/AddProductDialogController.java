package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.dao.ProductsDao;
import com.jawa.utsposclient.dto.BundleItem;
import com.jawa.utsposclient.dto.Product;
import com.jawa.utsposclient.entities.*;
import com.jawa.utsposclient.enums.ProductType;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.utils.*;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddProductDialogController extends Controller {
    @FXML private VBox expiryDateGroup;
    @FXML private VBox urlGroup;
    @FXML private VBox vendorGroup;
    @FXML private ComboBox<ProductType> typeComboBox;
    @FXML private TextField nameTextField;
    @FXML private TextField skuTextField;
    @FXML private TextField priceTextField;
    @FXML private DatePicker expiryDatePicker;
    @FXML private TextField urlTextField;
    @FXML private TextField vendorTextField;
    @FXML private VBox bundleGroup;
    @FXML private HBox selectedItemsContainer;

    private final List<BundleItem> bundleItems = new ArrayList<>();

    @FXML private void initialize() {
        List<ProductType> types = List.of(ProductType.values());
        typeComboBox.getItems().addAll(types);

        typeComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(ProductType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });
        typeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProductType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });

        var addItemButton = JawaButton.createExtendedFab(
            MaterialDesign.MDI_PLUS,
            StringRes.get("add_item_label"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        );
        addItemButton.setOnAction(e -> onAddBundleItemClicked());

        bundleGroup.getChildren().add(addItemButton);

        typeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            var itemCount = ProductsDao.getProductCountByType(newValue) + 1;
            skuTextField.setText(
                StringRes.getFormatted(
                    "sku_name_format",
                    newValue.getSkuCode(),
                    StringUtils.formatWithPrefix(itemCount)
                )
            );

            switch(newValue) {
                case NonPerishable -> {
                    expiryDateGroup.setVisible(false);
                    expiryDateGroup.setManaged(false);
                    urlGroup.setVisible(false);
                    urlGroup.setManaged(false);
                    vendorGroup.setVisible(false);
                    vendorGroup.setManaged(false);
                    bundleGroup.setVisible(false);
                    bundleGroup.setManaged(false);
                }
                case Perishable -> {
                    expiryDateGroup.setVisible(true);
                    expiryDateGroup.setManaged(true);
                    urlGroup.setVisible(false);
                    urlGroup.setManaged(false);
                    vendorGroup.setVisible(false);
                    vendorGroup.setManaged(false);
                    bundleGroup.setVisible(false);
                    bundleGroup.setManaged(false);
                }
                case Digital -> {
                    expiryDateGroup.setVisible(false);
                    expiryDateGroup.setManaged(false);
                    urlGroup.setVisible(true);
                    urlGroup.setManaged(true);
                    vendorGroup.setVisible(true);
                    vendorGroup.setManaged(true);
                    bundleGroup.setVisible(false);
                    bundleGroup.setManaged(false);
                }
                default -> {
                    expiryDateGroup.setVisible(false);
                    expiryDateGroup.setManaged(false);
                    urlGroup.setVisible(false);
                    urlGroup.setManaged(false);
                    vendorGroup.setVisible(false);
                    vendorGroup.setManaged(false);
                    bundleGroup.setVisible(true);
                    bundleGroup.setManaged(true);
                }
            }
        });

        typeComboBox.getSelectionModel().selectFirst();
        skuTextField.setEditable(false);
        skuTextField.setDisable(true);
    }

    @FXML
    public Long onAddProductAndGetId() {
        ProductType type = typeComboBox.getValue();
        String name = nameTextField.getText();
        String sku = skuTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        LocalDate expiryDate = expiryDatePicker.getValue();
        String url = urlTextField.getText();
        String vendor = vendorTextField.getText();

        if(name.trim().isEmpty() || price <= 0) throw new IllegalArgumentException("Invalid input!");

        switch(type) {
            case NonPerishable -> {
                NonPerishableProducts product = new NonPerishableProducts();
                product.setName(name);
                product.setSku(sku);
                product.setPrice(price);
                product.setAvailable(true);
                product.setType(ProductType.NonPerishable);
                return ProductRepository.addProductAndGetId(product);
            }
            case Perishable -> {
                if(expiryDate == null) throw new IllegalArgumentException("Invalid input!");
                PerishableProducts product = new PerishableProducts();
                product.setName(name);
                product.setSku(sku);
                product.setPrice(price);
                product.setAvailable(true);
                product.setType(ProductType.Perishable);
                product.setExpiryDate(DateUtils.localDateToDate(expiryDate));
                return ProductRepository.addProductAndGetId(product);
            }
            case Digital -> {
                if(url.trim().isEmpty() || vendor.trim().isEmpty()) throw new IllegalArgumentException("Invalid input!");
                DigitalProducts product = new DigitalProducts();
                product.setName(name);
                product.setSku(sku);
                product.setPrice(price);
                product.setAvailable(true);
                product.setType(ProductType.Digital);
                product.setUrl(url);
                product.setVendorName(vendor);
                return ProductRepository.addProductAndGetId(product);
            }
            default -> {
                BundleProducts product = new BundleProducts();
                product.setName(name);
                product.setSku(sku);
                product.setPrice(price);
                product.setAvailable(true);
                product.setType(ProductType.Bundle);
                var id = ProductRepository.addProductAndGetId(product);

                for (BundleItem item : bundleItems) {
                    Products productEntity = ProductRepository.getProductEntityById(item.getProduct().getId());

                    if (productEntity != null) {
                        BundleItems bundleItemEntity = new BundleItems();
                        bundleItemEntity.setBundleProduct(product);
                        bundleItemEntity.setProduct(productEntity);
                        bundleItemEntity.setQuantity(item.getQuantity());

                        ProductRepository.addBundleItem(bundleItemEntity);
                    } else {
                        System.err.println("Product not found for SKU: " + item.getProduct().getSku());
                    }
                }
                return id;
            }
        }
    }

    @FXML
    private void onAddBundleItemClicked() {
        List<Product> all = ProductRepository.getAllProducts().stream()
            .filter(p -> p.getType() != ProductType.Bundle)
            .toList();

        FramelessStyledAlert.showProductChoiceWithQuantity(all).ifPresent(item -> {
            var existing = bundleItems.stream()
                .filter(b -> b.getProduct().equals(item.getProduct()))
                .findFirst();

            if (existing.isPresent()) {
                existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
            } else {
                bundleItems.add(item);
            }

            updateSelectedItemsUI();
        });
    }

    private void updateSelectedItemsUI() {
        selectedItemsContainer.getChildren().clear();
        for (BundleItem item : bundleItems) {
            Button btn = new Button(item.toString());
            btn.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white;");
            btn.setOnAction(e -> {
                bundleItems.remove(item);
                updateSelectedItemsUI();
            });
            selectedItemsContainer.getChildren().add(btn);
        }
    }


}
