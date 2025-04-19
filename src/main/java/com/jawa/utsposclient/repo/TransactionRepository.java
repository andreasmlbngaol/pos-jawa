package com.jawa.utsposclient.repo;

import com.jawa.utsposclient.dao.TransactionDao;
import com.jawa.utsposclient.dto.PurchaseTransaction;

public class TransactionRepository {
    public static void executePurchaseTransaction(PurchaseTransaction transaction) {
        TransactionDao.insertPurchaseTransaction(transaction);
    }
}
