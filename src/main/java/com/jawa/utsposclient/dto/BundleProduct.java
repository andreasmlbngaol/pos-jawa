package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.ProductType;

import java.util.List;

@SuppressWarnings("unused")
public class BundleProduct extends Product {
    private List<BundleItem> items;

    public BundleProduct(
        Long id,
        String name,
        String sku,
        double price
    ) {
        super(id, name, sku, price, ProductType.Bundle);
    }

    public double getDiscountedPrice() {
        double total = 0;
        for(var item: items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }
}
