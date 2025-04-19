package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.dto.PurchaseTransaction;
import com.jawa.utsposclient.entities.PurchaseTransactions;
import com.jawa.utsposclient.entities.TransactionItems;

public class TransactionDao {
    public static void insertPurchaseTransaction(PurchaseTransaction transaction) {
        Database.executeVoidTransaction(session -> {
            PurchaseTransactions transactionEntity = new PurchaseTransactions();

            transactionEntity.setUser(UsersDao.getUserEntityByUsername(transaction.getUser().getUsername()));
            transactionEntity.setTotalAmount(transaction.getTotalAmount());
            transactionEntity.setCreatedAt(transaction.getCreatedAt());
            transactionEntity.setPaidAmount(transaction.getPaidAmount());
            transactionEntity.setChangeAmount(transaction.getChangeAmount());

            var items = transaction.getItems().stream()
                .map(dto -> {
                    System.out.println(dto.getPricePerItem());

                    TransactionItems entity = new TransactionItems();
                    entity.setProduct(ProductsDao.getProductEntityById(dto.getProduct().getId()));
                    entity.setQuantity(dto.getQuantity());
                    entity.setPricePerItem(dto.getPricePerItem());
                    entity.setTotalPrice(dto.getTotalPrice());
                    entity.setTransaction(transactionEntity);

                    return entity;
                })
                .toList();

            transactionEntity.setItems(items);
            session.persist(transactionEntity);
        });
    }
}
