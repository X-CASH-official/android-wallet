/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.security.MessageDigest;
import java.util.Arrays;

public class AppSignTool {

    static String TAG = "AppSignTool";

    public final static String ERROR = "error";
    public final static String MD5 = "MD5";
    public final static String SHA1 = "SHA1";
    public final static String SHA256 = "SHA256";

    public static String getSingInfo(Context context, String packageName, String type) {
        String result = ERROR;
        try {
            Signature[] signatures = getSignatures(context, packageName);
            Log.e(TAG, "signs =  " + Arrays.asList(signatures));
            Signature signature = signatures[0];
            if (type.equals(MD5)) {
                result = getSignatureString(signature, MD5);
            } else if (type.equals(SHA1)) {
                result = getSignatureString(signature, SHA1);
            } else if (type.equals(SHA256)) {
                result = getSignatureString(signature, SHA256);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static Signature[] getSignatures(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return packageInfo.signatures;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getSignatureString(Signature signature, String type) {
        String result = ERROR;
        if (signature == null) {
            return result;
        }
        try {
            byte[] hexBytes = signature.toByteArray();
            StringBuffer stringBuffer = new StringBuffer();
            MessageDigest digest = MessageDigest.getInstance(type);
            if (digest != null) {
                digest.reset();
                digest.update(hexBytes);
                byte[] byteArray = digest.digest();
                for (int i = 0; i < byteArray.length; i++) {
                    if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                        stringBuffer.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                    } else {
                        stringBuffer.append(Integer.toHexString(0xFF & byteArray[i]));
                    }
                }
                result = stringBuffer.toString().toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
