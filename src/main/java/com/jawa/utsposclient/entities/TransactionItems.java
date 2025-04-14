package com.jawa.utsposclient.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_items")
public class TransactionItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_id")
    private Transactions transaction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Products product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}
