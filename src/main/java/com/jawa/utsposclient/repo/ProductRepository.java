package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.dto.Product;
import com.jawa.utsposclient.entities.*;

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
}
