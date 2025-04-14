package com.jawa.utsposclient.dto;

import java.util.Date;
import java.util.List;

public class Transaction {
    private Long id;
    private Long userId;
    private double totalAmount;
    private Date createdAt;
    private List<TransactionItem> items;

    public static class TransactionItem {
        private Long productId;
        private String productName;
        private int quantity;
        private double price;
    }
}
