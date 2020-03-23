/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.xcash.utils.ClipboardTool;
import com.xcash.utils.CoroutineHelper;
import com.xcash.utils.QRCodeTool;
import com.xcash.wallet.uihelp.ActivityHelp;

public class ReceiveActivity extends NewBaseActivity {

    private String address;

    private int halfWidth;
    private ImageView imageViewBack;
    private TextView textViewTitle;
    private RelativeLayout relativeLayoutAddress;
    private TextView textViewAddress;
    private ImageView imageViewQRCode;

    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        Intent intent = getIntent();
        address = intent.getStringExtra(ActivityHelp.ADDRESS_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        halfWidth = BaseActivity.getScreenWidth(ReceiveActivity.this) / 2;
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        relativeLayoutAddress = (RelativeLayout) findViewById(R.id.relativeLayoutAddress);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        imageViewQRCode = (ImageView) findViewById(R.id.imageViewQRCode);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_receive_textViewTitle_text);
        if (address != null) {
            textViewAddress.setText(address);
        }
        TheApplication.setLayoutParams(imageViewQRCode, halfWidth, halfWidth);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
        showQRCode(address);
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.relativeLayoutAddress:
                        ClipboardTool.copyToClipboard(ReceiveActivity.this, textViewAddress.getText().toString());
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        relativeLayoutAddress.setOnClickListener(onClickListener);
    }

    private void showQRCode(final String address) {
        if (address == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Bitmap>() {
            @Override
            public Bitmap runOnIo() {
                return QRCodeTool.generateBitmap(address, halfWidth, halfWidth);
            }

            @Override
            public void overRunOnMain(Bitmap bitmap) {
                if (bitmap != null) {
                    imageViewQRCode.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

}
