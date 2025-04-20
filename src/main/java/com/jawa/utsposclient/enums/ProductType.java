package com.jawa.utsposclient.enums;

public enum ProductType {
    Perishable("PER", "Perishable"),
    NonPerishable("NPER", "Non Perishable"),
    Bundle("BNDL", "Bundle"),
    Digital("DIG", "Digital")

    ;

    private final String skuCode;
    private final String displayName;

    ProductType(String skuCode, String displayName) {
        this.skuCode = skuCode;
        this.displayName = displayName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getDisplayName() {
        return displayName;
    }
}
