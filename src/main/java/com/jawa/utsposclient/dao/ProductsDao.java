package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.dto.DigitalProduct;
import com.jawa.utsposclient.dto.NonPerishableProduct;
import com.jawa.utsposclient.dto.PerishableProduct;
import com.jawa.utsposclient.entities.*;
import com.jawa.utsposclient.enums.ProductType;
import com.jawa.utsposclient.utils.DateUtils;

public class ProductsDao {
    public static Long getProductCountByType(ProductType type) {
        return Database.executeTransaction(session -> session.createQuery(
            "SELECT COUNT(p) FROM Products p WHERE p.type = :type",
            Long.class
        )
        .setParameter("type", type)
        .uniqueResult());
    }

    public static Object getProductEntityBySku(String sku) {
        return Database.executeTransaction(session -> {
            var query = session.createQuery(
                "FROM Products p WHERE p.sku = :sku AND p.isAvailable=true",
                Object.class
            );
            query.setParameter("sku", sku);
            return query.uniqueResult();
        });
    }

    public static Products getProductEntityById(Long id) {
        return Database.executeTransaction(session -> session.get(Products.class, id));
    }

    public static void setProductUnavailable(Long id) {
        Database.executeVoidTransaction(session -> {
            var product = session.get(Products.class, id);
            if(product == null) throw new IllegalArgumentException("Product not found");
            product.setAvailable(false);
        });
    }

    public static Long addProductAndGetId(Products product) {
        return Database.executeTransaction(session -> {
            switch (product.getType()) {
                case NonPerishable -> {
                    var nonPerishable = (NonPerishableProducts) product;
                    session.persist(nonPerishable);
                    return nonPerishable.getId();
                }
                case Perishable -> {
                    var perishable = (PerishableProducts) product;
                    session.persist(perishable);
                    return perishable.getId();
                }

                case Digital -> {
                    var digital = (DigitalProducts) product;
                    session.persist(digital);
                    return digital.getId();
                }
                default -> {
                    var bundle = (BundleProducts) product;
                    session.persist(bundle);
                    return bundle.getId();
                }
            }
        });
    }

    public static void editPerishableProduct(PerishableProduct product) {
        Database.executeVoidTransaction(session -> {
            var perishable = session.get(PerishableProducts.class, product.getId());
            if(perishable == null) throw new IllegalArgumentException("Product not found");
            perishable.setName(product.getName());
            perishable.setPrice(product.getPrice());
            perishable.setExpiryDate(DateUtils.localDateToDate(product.getExpiryDate()));
        });
    }

    public static void editNonPerishableProduct(NonPerishableProduct product) {
        Database.executeVoidTransaction(session -> {
            var nonPerishable = session.get(NonPerishableProducts.class, product.getId());
            if(nonPerishable == null) throw new IllegalArgumentException("Product not found");
            nonPerishable.setName(product.getName());
            nonPerishable.setPrice(product.getPrice());
        });
    }

    public static void editDigitalProduct(DigitalProduct product) {
        Database.executeVoidTransaction(session -> {
            var digital = session.get(DigitalProducts.class, product.getId());
            if(digital == null) throw new IllegalArgumentException("Product not found");
            digital.setName(product.getName());
            digital.setPrice(product.getPrice());
            digital.setUrl(product.getUrl());
            digital.setVendorName(product.getVendorName());
        });
    }

    public static void addBundleItem(BundleItems item) {
        Database.executeVoidTransaction(session -> session.persist(item));
    }
}
