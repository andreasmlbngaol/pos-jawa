package com.jawa.utsposclient.dto;

public class PurchaseTransaction extends Transaction {
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
}
