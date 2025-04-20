package com.jawa.utsposclient.dao;

import com.jawa.utsposclient.db.Database;
import com.jawa.utsposclient.entities.Logs;
import com.jawa.utsposclient.enums.Activity;

public class LogsDao {
    public static void login(Long userId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.Login);
            log.setDescription(String.format("User dengan id %d login", userId));
            session.persist(log);
        });
    }

    public static void logout(Long userId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.Logout);
            log.setDescription(String.format("User dengan id %d logout", userId));
            session.persist(log);
        });
    }

    public static void addCashier(Long userId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.AddCashier);
            log.setDescription(String.format("User dengan id %d menambahkan kasir", userId));
            session.persist(log);
        });
    }

    public static void resetPassword(Long userId, Long cashierId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.ResetPassword);
            log.setDescription(String.format("User dengan id %d mengatur ulang kata sandi kasir dengan id %d", userId, cashierId));
            session.persist(log);
        });
    }

    public static void deleteCashier(Long userId, Long cashierId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.DeleteCashier);
            log.setDescription(String.format("User dengan id %d menghapus kasir dengan id %d", userId, cashierId));
            session.persist(log);
        });
    }

    public static void updateCashierName(Long userId, Long cashierId, String cashierName) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.EditCashier);
            log.setDescription(String.format("User dengan id %d mengubah nama kasir dengan id %d menjadi %s", userId, cashierId, cashierName));
            session.persist(log);
        });
    }

    public static void setPassword(Long userId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.SetPassword);
            log.setDescription(String.format("User dengan id %d mengatur kata sandi", userId));
            session.persist(log);
        });
    }

    public static void addProduct(Long userId, Long productId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.AddProduct);
            log.setDescription(String.format("User dengan id %d menambahkan produk dengan id %d", userId, productId));
            session.persist(log);
        });
    }

    public static void updateProduct(Long userId, Long productId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.EditProduct);
            log.setDescription(String.format("User dengan id %d mengubah produk dengan id %d", userId, productId));
            session.persist(log);
        });
    }

    public static void deleteProduct(Long userId, Long productId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.DeleteProduct);
            log.setDescription(String.format("User dengan id %d menghapus produk dengan id %d", userId, productId));
            session.persist(log);
        });
    }

    public static void createPurchaseTransaction(Long userId, Long purchaseTransactionId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.CreatePurchaseTransaction);
            log.setDescription(String.format("User dengan id %d membuat transaksi pembelian dengan id %d", userId, purchaseTransactionId));
            session.persist(log);
        });
    }

    public static void createRefundTransaction(Long userId, Long refundTransactionId) {
        Database.executeVoidTransaction(session -> {
            var log = new Logs();
            log.setUserId(userId);
            log.setActivity(Activity.CreateRefundTransaction);
            log.setDescription(String.format("User dengan id %d membuat transaksi pengembalian dengan id %d", userId, refundTransactionId));
            session.persist(log);
        });
    }
}
