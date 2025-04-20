package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.dto.NonPerishableProduct;
import com.jawa.utsposclient.repo.ProductRepository;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditNonPerishableDialogController extends Controller implements ProductDialogController {
    private NonPerishableProduct product;

    @FXML private TextField skuTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField priceTextField;

    public void setProduct(NonPerishableProduct product) {
        this.product = product;

        skuTextField.setText(product.getSku());
        nameTextField.setText(product.getName());
        priceTextField.setText(String.valueOf(product.getPrice()));
    }

    @Override
    public void onUpdateProduct() {
        var name = nameTextField.getText().trim();
        var price = Double.parseDouble(priceTextField.getText().trim());
        if(name.isEmpty() || price <= 0) throw new IllegalArgumentException("Invalid input!");

        product.setName(nameTextField.getText());
        product.setPrice(Double.parseDouble(priceTextField.getText()));

        ProductRepository.editNonPerishableProduct(product);
    }

}
