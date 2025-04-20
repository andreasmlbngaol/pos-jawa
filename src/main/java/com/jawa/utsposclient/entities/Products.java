package com.jawa.utsposclient.entities;

import com.jawa.utsposclient.enums.ProductType;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
    name = "products",
    indexes = {
        @Index(name = "idx_is_available", columnList =  "is_available")
    }
)
public abstract class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "is_available")
    private boolean isAvailable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase().trim();
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}