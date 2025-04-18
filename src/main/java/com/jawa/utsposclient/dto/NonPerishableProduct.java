package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.ProductType;

public class NonPerishableProduct extends Product {
    public NonPerishableProduct(
        Long id,
        String name,
        String sku,
        double price
    ) {
        super(id, name, sku, price, ProductType.NonPerishable);
    }
}
