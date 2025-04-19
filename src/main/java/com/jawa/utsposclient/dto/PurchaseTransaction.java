package com.jawa.utsposclient.dto;

import com.jawa.utsposclient.repo.TransactionRepository;
import com.jawa.utsposclient.utils.Payable;

public class PurchaseTransaction extends Transaction implements Payable {
    private Double paidAmount;
    private Double changeAmount;

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
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
        TransactionRepository.executePurchaseTransaction(this);
    }
}
