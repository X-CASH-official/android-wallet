/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet.uihelp;

import android.app.ProgressDialog;
import android.view.View;

import com.xcash.base.BaseActivity;
import com.xcash.base.utils.TimeTool;
import com.xcash.wallet.R;

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
