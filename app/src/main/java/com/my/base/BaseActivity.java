/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.my.base.utils.ActivityManager;
import com.my.base.utils.TimeTool;
import com.my.base.utils.ToastCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    public String activityKey = "error";
    public Handler handler = null;
    private boolean allowKeyBack = true;
    private HashMap<String, ProgressDialog> hashMapProgressDialogs = new HashMap<String, ProgressDialog>();
    private HashMap<String, PopupWindow> hashMapPopupWindows = new HashMap<String, PopupWindow>();

    public void addProgressDialog(String progressDialogKey, ProgressDialog progressDialog) {
        if (progressDialogKey != null && progressDialog != null) {
            hashMapProgressDialogs.put(progressDialogKey, progressDialog);
        }
    }

    public ProgressDialog getProgressDialog(String progressDialogKey) {
        if (progressDialogKey == null) {
            return null;
        }
        if (hashMapProgressDialogs.containsKey(progressDialogKey)) {
            return hashMapProgressDialogs.get(progressDialogKey);
        } else {
            return null;
        }
    }

    public void removeProgressDialog(String progressDialogKey) {
        if (progressDialogKey != null) {
            if (hashMapProgressDialogs.containsKey(progressDialogKey)) {
                hashMapProgressDialogs.remove(progressDialogKey);
            }
        }
    }

    public void dismissAndRemoveAllProgressDialog() {
        Iterator<Map.Entry<String, ProgressDialog>> iterator = hashMapProgressDialogs.entrySet().iterator();
        List<ProgressDialog> progressDialogs = new ArrayList<ProgressDialog>();
        while (iterator.hasNext()) {
            Map.Entry<String, ProgressDialog> entry = iterator.next();
            ProgressDialog progressDialog = entry.getValue();
            progressDialogs.add(progressDialog);
        }
        for (int i = 0; i < progressDialogs.size(); i++) {
            progressDialogs.get(i).dismiss();
        }
        hashMapProgressDialogs.clear();
    }


    public void addPopupWindow(String popupWindowKey, PopupWindow popupWindow) {
        if (popupWindowKey != null && popupWindow != null) {
            hashMapPopupWindows.put(popupWindowKey, popupWindow);
        }
    }

    public PopupWindow getPopupWindow(String popupWindowKey) {
        if (popupWindowKey == null) {
            return null;
        }
        if (hashMapPopupWindows.containsKey(popupWindowKey)) {
            return hashMapPopupWindows.get(popupWindowKey);
        } else {
            return null;
        }
    }

    public void removePopupWindow(String popupWindowKey) {
        if (popupWindowKey != null) {
            if (hashMapPopupWindows.containsKey(popupWindowKey)) {
                hashMapPopupWindows.remove(popupWindowKey);
            }
        }
    }

    public void dismissAndRemoveAllPopupWindow() {
        Iterator<Map.Entry<String, PopupWindow>> iterator = hashMapPopupWindows.entrySet().iterator();
        List<PopupWindow> popupWindows = new ArrayList<PopupWindow>();
        while (iterator.hasNext()) {
            Map.Entry<String, PopupWindow> entry = iterator.next();
            PopupWindow popupWindow = entry.getValue();
            popupWindows.add(popupWindow);
        }
        for (int i = 0; i < popupWindows.size(); i++) {
            popupWindows.get(i).dismiss();
        }
        hashMapPopupWindows.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityKey = getClass().getName() + ActivityManager.ACTIVITYKEYSEPARATOR + TimeTool.getOnlyTimeWithoutSleep();
        ActivityManager.getInstance().addBaseActivity(activityKey, this);
    }

    protected void initAll() {
        initHandler();
        initUi();
        initConfigUi();
        initHttp();
        initOther();
    }


    protected void initHandler() {

    }

    protected void initUi() {

    }

    protected void initConfigUi() {

    }

    protected void initHttp() {

    }

    protected void initOther() {

    }


    public void setWindowType(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            if (type == 1) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (type == 2) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean isAllowKeyBack() {
        return allowKeyBack;
    }

    public void setAllowKeyBack(boolean allowKeyBack) {
        this.allowKeyBack = allowKeyBack;
    }


    protected void doBack() {


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (allowKeyBack) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                doBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        dismissAndRemoveAllProgressDialog();
        dismissAndRemoveAllPopupWindow();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        ActivityManager.getInstance().removeBaseActivity(activityKey);
        super.onDestroy();
    }


    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static void showSystemErrorLog(String error) {
        if (error != null) {
            Log.e("SystemError", error);
        }
    }

    public static void showShortToast(Context context, String tips) {
        if (tips != null) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                ToastCompat.makeText(context.getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void showLongToast(Context context, String tips) {
        if (tips != null) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                ToastCompat.makeText(context.getApplicationContext(), tips, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context.getApplicationContext(), tips, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Activity getActivityByContext(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivityByContext(((ContextWrapper) context).getBaseContext());
        } else {
            return null;
        }
    }

}
