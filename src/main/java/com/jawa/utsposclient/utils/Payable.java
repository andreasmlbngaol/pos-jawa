package com.jawa.utsposclient.utils;

@SuppressWarnings("unused")
public interface Payable {
    double calculateTotal();
    void processTransaction();
    void serializeTransaction();
}
