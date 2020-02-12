/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StringTool {

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal bigDecimal = new BigDecimal(kiloByte);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal bigDecimal = new BigDecimal(megaByte);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraByte = gigaByte / 1024;
        if (teraByte < 1) {
            BigDecimal bigDecimal = new BigDecimal(gigaByte);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal bigDecimal = new BigDecimal(teraByte);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public static String formatDouble2(double number) {
        if (number == 0) {
            return "0.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(number);
    }

    public static long convertStringToLong(String content) {
        long blockHeight = 0;
        if (content == null || content.equals("")) {
            return blockHeight;
        }
        try {
            blockHeight = Long.parseLong(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return blockHeight;
    }

    public static String getDateTime(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    public static boolean checkWalletAddress(String address) {
        //support XCA|XCB and Monero address
        if (address.length() < 95) {
            return false;
        }
        return true;
    }

}
