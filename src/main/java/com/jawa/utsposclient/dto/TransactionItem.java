package com.jawa.utsposclient.dto;

public class TransactionItem {
    private Long id;
    private final Product product;
    private int quantity;
    private Double pricePerItem;
    private Double totalPrice;

    public TransactionItem(Product product) {
        this.product = product;
        this.pricePerItem = product.getPrice();
        setQuantity(1);
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void calculateTotalPrice() {
        this.totalPrice = this.quantity * this.pricePerItem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.calculateTotalPrice();
    }

    public Double getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(Double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
