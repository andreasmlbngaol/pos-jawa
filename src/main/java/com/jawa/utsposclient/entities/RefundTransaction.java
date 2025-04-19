package com.jawa.utsposclient.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "refund_transaction")
@PrimaryKeyJoinColumn(name = "transaction_id")
public class RefundTransaction extends Transactions {

    @Column(name = "refund_reason")
    String refundReason;

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
}
