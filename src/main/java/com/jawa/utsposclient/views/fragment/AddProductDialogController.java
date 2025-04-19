package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.dao.ProductsDao;
import com.jawa.utsposclient.entities.DigitalProducts;
import com.jawa.utsposclient.entities.NonPerishableProducts;
import com.jawa.utsposclient.entities.PerishableProducts;
import com.jawa.utsposclient.enums.ProductType;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.utils.StringUtils;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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


    @FXML private void initialize() {
        List<ProductType> types = List.of(ProductType.values());
        typeComboBox.getItems().addAll(types);

        typeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            var itemCount = ProductsDao.getProductCountByType(newValue) + 1;
            skuTextField.setText(String.format("%s-%s", newValue.getSkuCode(), StringUtils.formatWithPrefix(itemCount)));

            switch(newValue) {
                case NonPerishable -> {
                    expiryDateGroup.setVisible(false);
                    expiryDateGroup.setManaged(false);
                    urlGroup.setVisible(false);
                    urlGroup.setManaged(false);
                    vendorGroup.setVisible(false);
                    vendorGroup.setManaged(false);
                }
                case Perishable -> {
                    expiryDateGroup.setVisible(true);
                    expiryDateGroup.setManaged(true);
                    urlGroup.setVisible(false);
                    urlGroup.setManaged(false);
                    vendorGroup.setVisible(false);
                    vendorGroup.setManaged(false);
                }
                case Digital -> {
                    expiryDateGroup.setVisible(false);
                    expiryDateGroup.setManaged(false);
                    urlGroup.setVisible(true);
                    urlGroup.setManaged(true);
                    vendorGroup.setVisible(true);
                    vendorGroup.setManaged(true);
                }
                default -> {
                    expiryDateGroup.setVisible(false);
                    expiryDateGroup.setManaged(false);
                    urlGroup.setVisible(false);
                    urlGroup.setManaged(false);
                    vendorGroup.setVisible(false);
                    vendorGroup.setManaged(false);
                }
            }
        });

        typeComboBox.getSelectionModel().selectFirst();
        skuTextField.setEditable(false);
        skuTextField.setDisable(true);
    }

    @FXML
    public void onAddProduct() {
        ProductType type = typeComboBox.getValue();
        String name = nameTextField.getText();
        String sku = skuTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        LocalDate expiryDate = expiryDatePicker.getValue();
        String url = urlTextField.getText();
        String vendor = vendorTextField.getText();

        switch(type) {
            case NonPerishable -> {
                NonPerishableProducts product = new NonPerishableProducts();
                product.setName(name);
                product.setSku(sku);
                product.setPrice(price);
                product.setAvailable(true);
                product.setType(ProductType.NonPerishable);
                ProductRepository.addProduct(product);
            }
            case Perishable -> {
                PerishableProducts product = new PerishableProducts();
                product.setName(name);
                product.setSku(sku);
                product.setPrice(price);
                product.setAvailable(true);
                product.setType(ProductType.Perishable);
                product.setExpiryDate(Date.from(expiryDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                ProductRepository.addProduct(product);
            }
            case Digital -> {
                DigitalProducts product = new DigitalProducts();
                product.setName(name);
                product.setSku(sku);
                product.setPrice(price);
                product.setAvailable(true);
                product.setType(ProductType.Digital);
                product.setUrl(url);
                product.setVendorName(vendor);
                ProductRepository.addProduct(product);
            }
            default -> {

            }
        }
    }


}
