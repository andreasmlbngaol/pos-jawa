package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.enums.ProductType;

public class DigitalProduct extends Product {
    private String url;
    private String vendorName;

    public DigitalProduct(
        Long id,
        String name,
        String sku,
        double price,
        String url,
        String vendorName
    ) {
        super(id, name, sku, price, ProductType.Digital);
        this.url = url;
        this.vendorName = vendorName;
    }

    public String getUrl() {
        return url;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
