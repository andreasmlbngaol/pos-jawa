package com.jawa.utsposclient.dto;

import java.util.List;

public class BundleProduct extends Product {
    private List<BundleItem> items;

    public static class BundleItem {
        private Long productId;
        private String name;
        private int quantity;
    }
}
