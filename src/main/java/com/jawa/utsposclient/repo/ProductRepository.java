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
        return Database.executeTransaction(session -> session.get(Products.class, id));
    }

    public static void softDelete(Long id) {
        ProductsDao.setProductUnavailable(id);
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
