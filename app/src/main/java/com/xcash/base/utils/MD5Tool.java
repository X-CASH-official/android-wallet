/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.base.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tool {

    public static String stringToMD5(String string) {
        if (string == null) {
            return null;
        }
        byte[] byteHashs = null;
        try {
            byteHashs = MessageDigest.getInstance("MD5").digest(string.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder stringBuilderHex = null;
        if (byteHashs != null) {
            stringBuilderHex = new StringBuilder(byteHashs.length * 2);
            for (int i = 0; i < byteHashs.length; i++) {
                byte theByte = byteHashs[i];
                if ((theByte & 0xff) < 0x10) {
                    stringBuilderHex.append("0");
                }
                stringBuilderHex.append(Integer.toHexString(theByte & 0xff));
            }
        }
        if (stringBuilderHex == null) {
            return null;
        } else {
            return stringBuilderHex.toString();
        }
    }

}
