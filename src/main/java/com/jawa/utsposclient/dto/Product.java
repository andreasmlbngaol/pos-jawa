package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.ProductType;

@SuppressWarnings("unused")
public class Product {
    private Long id;
    private String name;
    private String sku;
    private double price;
    private ProductType type;

    public Product(
        Long id,
        String name,
        String sku,
        double price,
        ProductType type
    ) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }
}
