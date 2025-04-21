package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.dto.PurchaseTransaction;
import com.jawa.utsposclient.dto.RefundTransaction;
import com.jawa.utsposclient.entities.PurchaseTransactions;
import com.jawa.utsposclient.entities.RefundTransactions;
import com.jawa.utsposclient.entities.TransactionItems;

import java.util.List;

public class TransactionsDao {
    public static Long insertPurchaseTransaction(PurchaseTransaction transaction) {
        return Database.executeTransaction(session -> {
            PurchaseTransactions transactionEntity = new PurchaseTransactions();

            transactionEntity.setUser(UsersDao.getUserEntityByUsername(transaction.getUser().getUsername()));
            transactionEntity.setTotalAmount(transaction.getTotalAmount());
            transactionEntity.setCreatedAt(transaction.getCreatedAt());
            transactionEntity.setPaidAmount(transaction.getPaidAmount());
            transactionEntity.setChangeAmount(transaction.getChangeAmount());

            var items = transaction.getItems().stream()
                .map(dto -> {
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
            return transactionEntity.getId();
        });
    }

    public static boolean getRefundTransactionWithPurchaseId(Long purchaseTransactionId) {
        return Database.executeTransaction(session -> {
            var query = session.createQuery(
                "SELECT r FROM RefundTransactions r WHERE r.purchaseTransaction.id = :purchaseTransactionId",
                RefundTransactions.class
            );
            query.setParameter("purchaseTransactionId", purchaseTransactionId);
            return query.uniqueResultOptional().isPresent();
        });
    }

    public static Long insertRefundTransaction(RefundTransaction transaction) {
        return Database.executeTransaction(session -> {
            RefundTransactions transactionEntity = new RefundTransactions();

            // Set data dasar transaksi
            transactionEntity.setUser(UsersDao.getUserEntityByUsername(transaction.getUser().getUsername()));
            transactionEntity.setTotalAmount(transaction.getTotalAmount());
            transactionEntity.setCreatedAt(transaction.getCreatedAt());
            transactionEntity.setRefundReason(transaction.getRefundReason());

            // Konversi item-item DTO ke entitas
            var items = transaction.getItems().stream()
                .map(dto -> {
                    TransactionItems entity = new TransactionItems();
                    entity.setProduct(ProductsDao.getProductEntityById(dto.getProduct().getId()));
                    entity.setQuantity(dto.getQuantity());
                    entity.setPricePerItem(dto.getPricePerItem());
                    entity.setTotalPrice(dto.getTotalPrice());
                    entity.setTransaction(transactionEntity); // sangat penting untuk relasi

                    return entity;
                })
                .toList();

            transactionEntity.setItems(items);

            var purchaseTransactionEntity = getPurchaseTransactionEntityById(transaction.getPurchaseTransaction().getId());
            transactionEntity.setPurchaseTransaction(purchaseTransactionEntity);

            // Simpan ke DB
            session.persist(transactionEntity);
            return transactionEntity.getId();
        });
    }

    public static PurchaseTransactions getPurchaseTransactionEntityById(Long id) {
        return Database.executeTransaction(session -> session.get(PurchaseTransactions.class, id));
    }

    public static List<PurchaseTransactions> getAllPurchaseTransactions() {
        return Database.executeTransaction(session ->
            session.createQuery("FROM PurchaseTransactions p ORDER BY p.id", PurchaseTransactions.class).getResultList()
        );
    }
}
