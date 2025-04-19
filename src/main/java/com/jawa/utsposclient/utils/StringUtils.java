package com.jawa.utsposclient.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {
    public static String moneyFormat(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
//        return String.format(Locale.of("id", "ID"),"%,.0f", amount);
        return formatter.format(amount);
    }

    public static String formatWithPrefix(long number) {
        return String.format("%04d", number);
    }

    public static String toValidSecretKey(String plainKey, int length) {
        if(plainKey.length() < length) {
            return String.format("%-" + length + "s", plainKey).replace(' ', '0');
        } else if(plainKey.length() > length) {
            return plainKey.substring(0, length);
        }
        return plainKey;
    }
}
