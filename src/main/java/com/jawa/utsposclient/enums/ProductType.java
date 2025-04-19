package com.jawa.utsposclient.enums;

public enum ProductType {
    Perishable("PER"),
    NonPerishable("NPER"),
    Bundle("BNDL"),
    Digital("DIG")

    ;

    private final String skuCode;

    ProductType(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuCode() {
        return skuCode;
    }
}
