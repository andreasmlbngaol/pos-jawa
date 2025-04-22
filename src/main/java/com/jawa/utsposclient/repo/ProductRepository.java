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
                .map(product -> {
                    switch (product.getType()) {
                        case Perishable -> {
                            var perishable = (PerishableProducts) product;
                            var expiryDate = DateUtils.dateToLocalDate(perishable.getExpiryDate());
                            return new PerishableProduct(
                                product.getId(),
                                product.getName(),
                                product.getSku(),
                                product.getPrice(),
                                expiryDate
                            );
                        }
                        case NonPerishable -> {
                            return new NonPerishableProduct(
                                product.getId(),
                                product.getName(),
                                product.getSku(),
                                product.getPrice()
                            );
                        }
                        case Digital -> {
                            var digital = (DigitalProducts) product;
                            return new DigitalProduct(
                                product.getId(),
                                product.getName(),
                                product.getSku(),
                                product.getPrice(),
                                digital.getUrl(),
                                digital.getVendorName()
                            );
                        }
                        default -> {
                            return new BundleProduct(
                                product.getId(),
                                product.getName(),
                                product.getSku(),
                                product.getPrice()
                            );
                        }
                    }
                })
                .collect(Collectors.toList());
        });
    }

    public static Long addProductAndGetId(Products product) {
        return ProductsDao.addProductAndGetId(product);
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

    public static Products getProductEntityById(Long id) {
        return ProductsDao.getProductEntityById(id);
    }

    public static void softDelete(Long id) {
        ProductsDao.setProductUnavailable(id);
    }

    public static void editPerishableProduct(PerishableProduct perishable) {
        ProductsDao.editPerishableProduct(perishable);
    }

    public static void editNonPerishableProduct(NonPerishableProduct nonPerishable) {
        ProductsDao.editNonPerishableProduct(nonPerishable);
    }

    public static void editDigitalProduct(DigitalProduct digital) {
        ProductsDao.editDigitalProduct(digital);
    }

    public static void addBundleItem(BundleItems item) {
        ProductsDao.addBundleItem(item);
    }
}
