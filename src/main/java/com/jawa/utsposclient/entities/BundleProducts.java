package com.jawa.utsposclient.entities;

import jakarta.persistence.*;

 import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "bundle_products")
@PrimaryKeyJoinColumn(name = "product_id")
public class BundleProducts extends Products {

    @OneToMany(mappedBy = "bundleProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BundleItems> bundleItems;

    public List<BundleItems> getBundleItems() {
        return bundleItems;
    }

    public List<Products> getProductsInBundle() {
        return bundleItems.stream()
            .map(BundleItems::getProduct)
            .collect(Collectors.toList());
    }

    public BundleProducts() {}
}
