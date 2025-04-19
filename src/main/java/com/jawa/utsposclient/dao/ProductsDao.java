package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
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
}
