 /*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xcash.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;

public class AppSignTool {

    public final static String ERROR = "error";
    public final static String MD5 = "MD5";
    public final static String SHA1 = "SHA1";
    public final static String SHA256 = "SHA256";

    public static String getSingInfo(Context context, String packageName, String type) {
        String result = ERROR;
        try {
            Signature[] signatures = getSignatures(context, packageName);
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
