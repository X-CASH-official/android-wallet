/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.my.base.BaseActivity;
import com.my.utils.LanguageTool;

public class NewBaseActivity extends BaseActivity {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LanguageTool.initAppLanguage(context, TheApplication.getSetting().getLanguage()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    public void adaptationStatusBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams = null;
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            } else {
                marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
            }
            marginLayoutParams.topMargin = getStatusBarHeight();
            view.setLayoutParams(marginLayoutParams);
        }
    }

}
