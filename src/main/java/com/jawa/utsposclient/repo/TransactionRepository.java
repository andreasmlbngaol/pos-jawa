package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.dao.TransactionsDao;
import com.jawa.utsposclient.dto.*;

public class TransactionRepository {
    public static void executePurchaseTransaction(PurchaseTransaction transaction) {
        TransactionsDao.insertPurchaseTransaction(transaction);
    }

    public static void executeRefundTransaction(RefundTransaction transaction) {
        TransactionsDao.insertRefundTransaction(transaction);
    }

    public static boolean isRefundTransactionExist(Long purchaseTransactionId) {
        return TransactionsDao.getRefundTransactionWithPurchaseId(purchaseTransactionId);
    }

    public static PurchaseTransaction getPurchaseTransactionById(Long id) {
        var transactionEntity = TransactionsDao.getPurchaseTransactionEntityById(id);
        if(transactionEntity == null) return null;

        var transaction =  new PurchaseTransaction();
        transaction.setId(transactionEntity.getId());
        transaction.setUser(new User(transactionEntity.getUser()));
        transaction.setTotalAmount(transactionEntity.getTotalAmount());
        transaction.setCreatedAt(transactionEntity.getCreatedAt());
        transaction.setPaidAmount(transactionEntity.getPaidAmount());
        transaction.setChangeAmount(transactionEntity.getChangeAmount());
        var item = transactionEntity.getItems().stream()
            .map(entity -> {
                TransactionItem dto = new TransactionItem();
                dto.setId(entity.getId());
                dto.setProduct(new Product(entity.getProduct()));
                dto.setQuantity(entity.getQuantity());
                return dto;
            })
            .toList();
        transaction.setItems(item);

        return transaction;
    }
}
