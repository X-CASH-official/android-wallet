/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.aidl.OnNormalListener;
import com.xcash.wallet.aidl.WalletOperateManager;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.ProgressDialogHelp;


public class DpopsRemoveActivity extends NewBaseActivity {

    private Wallet wallet;

    private int colorPrimary;
    private int mainColorText;
    private int textView_error;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private TextView textViewTips;
    private Button buttonNext;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpops_remove);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
        initAll();
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArrayColor() {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.mainColorText});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
            mainColorText = typedArray.getColor(1, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        getTypeArrayColor();
        textView_error = ContextCompat.getColor(DpopsRemoveActivity.this, R.color.textView_error);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTips = (TextView) findViewById(R.id.textViewTips);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_dpops_remove_textViewTitle_text);
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
                    case R.id.buttonNext:
                        doRemove(buttonNext);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void doRemove(final View view) {
        if (wallet == null) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(DpopsRemoveActivity.this, view);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            final String content = "Result=> ";
            walletOperateManager.delegateRemove(new OnNormalListener.Stub() {
                @Override
                public void onSuccess(final String tips) throws RemoteException {
                    DpopsActivity.addOperationHistory(wallet.getId(), "Delegate Remove", true, content + tips);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTips.setText(tips);
                            textViewTips.setTextColor(colorPrimary);
                            ProgressDialogHelp.enabledView(DpopsRemoveActivity.this, progressDialog, progressDialogKey, view);
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    DpopsActivity.addOperationHistory(wallet.getId(), "Delegate Remove", false, content + error);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTips.setText(error);
                            textViewTips.setTextColor(textView_error);
                            ProgressDialogHelp.enabledView(DpopsRemoveActivity.this, progressDialog, progressDialogKey, view);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(DpopsRemoveActivity.this, progressDialog, progressDialogKey, view);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
