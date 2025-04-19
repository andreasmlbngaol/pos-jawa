package com.jawa.utsposclient.entities;

import jakarta.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "refund_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
public class RefundTransactions extends Transactions {

    @OneToOne
    @JoinColumn(name = "purchase_transaction_id")
    PurchaseTransactions purchaseTransaction;

    @Column(name = "refund_reason")
    String refundReason;

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public PurchaseTransactions getPurchaseTransaction() {
        return purchaseTransaction;
    }

    public void setPurchaseTransaction(PurchaseTransactions purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction;
    }
}
