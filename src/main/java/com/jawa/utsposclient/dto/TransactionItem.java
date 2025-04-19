package com.jawa.utsposclient.dto;

public class TransactionItem {
    private final Product product;
    private int quantity;

    public TransactionItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    public String getSku() {
        return product.getSku();
    }

    public String getName() {
        return product.getName();
    }

}
