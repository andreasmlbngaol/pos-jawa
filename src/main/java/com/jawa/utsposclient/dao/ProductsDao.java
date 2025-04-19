package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.entities.Products;
import com.jawa.utsposclient.enums.ProductType;

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
}
