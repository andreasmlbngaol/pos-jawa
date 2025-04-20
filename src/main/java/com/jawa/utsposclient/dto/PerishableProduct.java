package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.ProductType;

import java.time.LocalDate;

public class PerishableProduct extends Product {
    private LocalDate expiryDate;

    public PerishableProduct(
        Long id,
        String name,
        String sku,
        double price,
        LocalDate expiryDate
    ) {
        super(id, name, sku, price, ProductType.Perishable);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
