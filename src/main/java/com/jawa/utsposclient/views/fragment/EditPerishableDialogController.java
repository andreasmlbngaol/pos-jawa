package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.dto.PerishableProduct;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class EditPerishableDialogController extends Controller implements ProductDialogController {
    private PerishableProduct product;

    @FXML private TextField skuTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField priceTextField;
    @FXML private DatePicker expiryDatePicker;

    public void setProduct(PerishableProduct product) {
        this.product = product;

        skuTextField.setText(product.getSku());
        nameTextField.setText(product.getName());
        priceTextField.setText(String.valueOf(product.getPrice()));
        expiryDatePicker.setValue(product.getExpiryDate());
    }

    @FXML

    @Override
    public void onUpdateProduct() {
        var name = nameTextField.getText().trim();
        var price = Double.parseDouble(priceTextField.getText().trim());
        var expiryDate = expiryDatePicker.getValue();

        if(name.isEmpty() || price <= 0 || expiryDate == null) throw new IllegalArgumentException("Invalid input!");

        product.setName(nameTextField.getText());
        product.setPrice(Double.parseDouble(priceTextField.getText()));
        product.setExpiryDate(product.getExpiryDate());

        ProductRepository.editPerishableProduct(product);
    }
}
