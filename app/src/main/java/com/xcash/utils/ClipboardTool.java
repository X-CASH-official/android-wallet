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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.xcash.base.BaseActivity;
import com.xcash.testnetwallet.R;

public class ClipboardTool {

    public static void copyToClipboard(Context context, String content) {
        if (content == null || content.equals("")) {
            return;
        }
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Label", content);
            clipboardManager.setPrimaryClip(clipData);
            BaseActivity.showShortToast(context, context.getString(R.string.normal_copySuccess_tips));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
