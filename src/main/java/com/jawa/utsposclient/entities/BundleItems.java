package com.jawa.utsposclient.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bundle_items")
public class BundleItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "bundle_id", nullable = false)
    private BundleProducts bundleProduct;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @Column(nullable = false)
    private int quantity;

    public BundleItems() {}

    public BundleProducts getBundleProduct() {
        return bundleProduct;
    }

    public Products getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBundleProduct(BundleProducts bundleProduct) {
        this.bundleProduct = bundleProduct;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
