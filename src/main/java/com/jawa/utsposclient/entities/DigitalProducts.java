package com.jawa.utsposclient.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name = "digital_products")
@PrimaryKeyJoinColumn(name = "product_id")
public class DigitalProducts extends Products {

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, name = "vendor_name")
    private String vendorName;

    public DigitalProducts() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
