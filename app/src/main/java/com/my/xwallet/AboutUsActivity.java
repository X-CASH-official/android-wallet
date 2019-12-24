/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.utils.MachineInformationTool;
import com.my.xwallet.uihelp.ActivityHelp;


public class AboutUsActivity extends NewBaseActivity {

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private TextView textViewVersion;
    private TextView textViewFindNewVersion;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initAll();
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewVersion = (TextView) findViewById(R.id.textViewVersion);
        textViewFindNewVersion = (TextView) findViewById(R.id.textViewFindNewVersion);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_about_us_textViewTitle_text);
        textViewVersion.setText("v" + MachineInformationTool.getVersionName(AboutUsActivity.this));
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.textViewFindNewVersion:
                        Intent intent = new Intent(AboutUsActivity.this,
                                WebViewActivity.class);
                        intent.putExtra(ActivityHelp.REQUEST_TITLE_KEY, getString(R.string.activity_main_navigationview_textViewAboutUs_text));
                        intent.putExtra(ActivityHelp.REQUEST_URL_KEY, getString(R.string.about_us_url));
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        textViewFindNewVersion.setOnClickListener(onClickListener);
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
