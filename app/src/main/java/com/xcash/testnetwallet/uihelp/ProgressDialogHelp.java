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
package com.xcash.testnetwallet.uihelp;

import android.app.ProgressDialog;
import android.view.View;

import com.xcash.base.BaseActivity;
import com.xcash.base.utils.TimeTool;
import com.xcash.testnetwallet.R;

public class ProgressDialogHelp {

    public static Object[] unEnabledView(BaseActivity baseActivity, View view) {
        if (view != null) {
            view.setEnabled(false);
        }
        ProgressDialog progressDialog = ProgressDialog.show(baseActivity, null, baseActivity.getString(R.string.progressDialogHelp_tips), false, false);
        String progressDialogKey = "progressDialogKey" + TimeTool.getOnlyTimeWithoutSleep();
        baseActivity.addProgressDialog(progressDialogKey, progressDialog);
        Object[] objects = new Object[2];
        objects[0] = progressDialog;
        objects[1] = progressDialogKey;
        return objects;
    }

    public static Object[] unEnabledView(BaseActivity baseActivity, View view, ProgressDialog progressDialog) {
        if (view != null) {
            view.setEnabled(false);
        }
        progressDialog.show();
        String progressDialogKey = "progressDialogKey" + TimeTool.getOnlyTimeWithoutSleep();
        baseActivity.addProgressDialog(progressDialogKey, progressDialog);
        Object[] objects = new Object[2];
        objects[0] = progressDialog;
        objects[1] = progressDialogKey;
        return objects;
    }

    public static void enabledView(BaseActivity baseActivity, ProgressDialog progressDialog, String progressDialogKey, View view) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        baseActivity.removeProgressDialog(progressDialogKey);
        if (view != null) {
            view.setEnabled(true);
        }
    }

}
