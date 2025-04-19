package com.jawa.utsposclient.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
public class PurchaseTransactions extends Transactions {
    @Column(nullable = false, name = "paid_amount")
    private double paidAmount;

    @Column(nullable = false, name = "change_amount")
    private double changeAmount;

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }
}
