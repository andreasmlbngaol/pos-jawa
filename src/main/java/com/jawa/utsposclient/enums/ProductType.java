package com.jawa.utsposclient.enums;

import com.jawa.utsposclient.utils.StringRes;

public enum ProductType {
    Perishable("PER", StringRes.get("perishable_product_display_name")),
    NonPerishable("NPER", StringRes.get("non_perishable_product_display_name")),
    Bundle("BNDL",StringRes.get( "bundle_product_display_name")),
    Digital("DIG", StringRes.get("digital_product_display_name"))

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
