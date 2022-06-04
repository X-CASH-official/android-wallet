package com.xcash.utils;

import java.lang.String;
import java.text.DecimalFormat;

public class CompactNumberFormatter {
    public static final int XCASH_DECIMALS = 6;

    public static int getXCASHDecimals() {
        return XCASH_DECIMALS;
    }

    // FIXME: unexpected behavior for negative or large doubles
    public static String formatValue(double value) {
        String[] arr = new String[]{"", "K", "M", "B", "T", "P", "E"};
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1_000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return String.format("%s%s", decimalFormat.format(value), arr[index]);
    }

}
