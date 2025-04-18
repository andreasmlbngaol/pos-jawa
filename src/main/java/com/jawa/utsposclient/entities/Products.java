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
    private double price;

//    @Column(nullable = false)
//    private int stock;
//
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "is_available")
    private boolean isAvailable;
}