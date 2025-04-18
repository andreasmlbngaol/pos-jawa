package com.jawa.utsposclient.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "non_perishable_products")
@PrimaryKeyJoinColumn(name = "product_id")
public class NonPerishableProducts extends Products {
    public NonPerishableProducts() {}

}
