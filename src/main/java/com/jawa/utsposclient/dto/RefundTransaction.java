package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.repo.TransactionRepository;
import com.jawa.utsposclient.utils.Payable;

public class RefundTransaction extends Transaction implements Payable {
    private PurchaseTransaction purchaseTransaction;
    private String refundReason;

    public PurchaseTransaction getPurchaseTransaction() {
        return purchaseTransaction;
    }

    public void setPurchaseTransaction(PurchaseTransaction purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    @Override
    public double calculateTotal() {
        return super.getTotalAmount();
    }

    @Override
    public void processTransaction() {
        this.serializeTransaction();
    }

    @Override
    public void serializeTransaction() {
        TransactionRepository.executeRefundTransaction(this);
    }
}
