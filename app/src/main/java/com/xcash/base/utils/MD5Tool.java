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
