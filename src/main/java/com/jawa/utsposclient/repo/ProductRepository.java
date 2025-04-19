package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.dao.ProductsDao;
import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.dto.*;
import com.jawa.utsposclient.entities.*;
import com.jawa.utsposclient.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {
    public static List<Product> getAllProducts() {
        return Database.executeTransaction(session -> {
            List<Products> productEntities = session.createQuery("FROM Products p WHERE p.isAvailable = true ORDER BY p.id", Products.class).getResultList();
            return productEntities.stream()
                .map(product -> new Product(
                    product.getId(),
                    product.getName(),
                    product.getSku(),
                    product.getPrice(),
                    product.getType()
                ))
                .collect(Collectors.toList());
        });
    }

    public static void addProduct(Products product) {
        Database.executeVoidTransaction(session -> {
            switch (product.getType()) {
                case NonPerishable -> {
                    var nonPerishable = (NonPerishableProducts) product;
                    session.persist(nonPerishable);
                }
                case Perishable -> {
                    var perishable = (PerishableProducts) product;
                    session.persist(perishable);
                }

                case Digital -> {
                    var digital = (DigitalProducts) product;
                    session.persist(digital);
                }
                default -> {
                    var bundle = (BundleProducts) product;
                    session.persist(bundle);
                }
            }
        });
    }

    public static Product getProductBySku(String sku) {
        var object = ProductsDao.getProductEntityBySku(sku);
        if(object == null) return null;

        var product = (Products) object;

        var id = product.getId();
        var name = product.getName();
        var price = product.getPrice();
        var type = product.getType();

        switch (type) {
            case NonPerishable -> {
                return new NonPerishableProduct(id, name, sku, price);
            }
            case Perishable -> {
                var perishable = (PerishableProducts) product;
                var expiryDate = DateUtils.dateToLocalDate(perishable.getExpiryDate());
                return new PerishableProduct(id, name, sku, price, expiryDate);
            }
            case Digital -> {
                var digital = (DigitalProducts) product;
                var url = digital.getUrl();
                var vendorName = digital.getVendorName();
                return new DigitalProduct(id, name, sku, price, url, vendorName);
            }
            default -> {
                return new BundleProduct(id, name, sku, price);
            }
        }
    }
}
