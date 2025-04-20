package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.dto.DigitalProduct;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditDigitalDialogController extends Controller implements ProductDialogController {
    private DigitalProduct product;

    @FXML private TextField skuTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField urlTextField;
    @FXML private TextField vendorTextField;

    public void setProduct(DigitalProduct product) {
        this.product = product;
        skuTextField.setText(product.getSku());
        nameTextField.setText(product.getName());
        priceTextField.setText(String.valueOf(product.getPrice()));
        urlTextField.setText(product.getUrl());
        vendorTextField.setText(product.getVendorName());
    }

    @Override
    public void onUpdateProduct() {
        var name = nameTextField.getText().trim();
        var price = Double.parseDouble(priceTextField.getText().trim());
        var url = urlTextField.getText().trim();
        var vendorName = vendorTextField.getText().trim();
        if(name.isEmpty() || price <= 0 || url.isEmpty() || vendorName.isEmpty()) throw new IllegalArgumentException("Invalid input!");

        product.setName(name);
        product.setPrice(price);
        product.setUrl(url);
        product.setVendorName(vendorName);

        ProductRepository.editDigitalProduct(product);
    }
}
