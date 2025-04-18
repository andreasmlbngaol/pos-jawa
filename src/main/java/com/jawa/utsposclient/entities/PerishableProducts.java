package com.jawa.utsposclient.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "perishable_products")
@PrimaryKeyJoinColumn(name = "product_id")
public class PerishableProducts extends Products {

    @Column(nullable = false, name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    public PerishableProducts() {}

    @SuppressWarnings("unused")
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
