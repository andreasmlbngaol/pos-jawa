package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.ProductType;

import java.util.List;

public class BundleProduct extends Product {
    private List<BundleItem> items;

    public static class BundleItem {
        private Long productId;
        private String name;
        private int quantity;
    }

    public BundleProduct(
        Long id,
        String name,
        String sku,
        double price
    ) {
        super(id, name, sku, price, ProductType.Bundle);
    }
}
