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
package com.xcash.testnetwallet;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xcash.base.BaseActivity;
import com.xcash.utils.WalletServiceHelper;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.testnetwallet.aidl.OnNormalListener;
import com.xcash.testnetwallet.aidl.WalletOperateManager;
import com.xcash.testnetwallet.uihelp.ActivityHelp;
import com.xcash.testnetwallet.uihelp.ProgressDialogHelp;


public class DpopsRegisterActivity extends NewBaseActivity {

    private Wallet wallet;

    private int colorPrimary;
    private int mainColorText;
    private int textView_error;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private EditText editTextDelegateName;
    private FrameLayout frameLayoutDelegateName;
    private EditText editTextDelegateIPAddress;
    private FrameLayout frameLayoutDelegateIPAddress;
    private EditText editTextBlockVerifierMessagesPublicKey;
    private FrameLayout frameLayoutBlockVerifierPublicKey;
    private TextView textViewTips;
    private Button buttonNext;

    private View.OnFocusChangeListener onFocusChangeListener;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpops_register);
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
        textView_error = ContextCompat.getColor(DpopsRegisterActivity.this, R.color.textView_error);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        editTextDelegateName = (EditText) findViewById(R.id.editTextDelegateName);
        frameLayoutDelegateName = (FrameLayout) findViewById(R.id.frameLayoutDelegateName);
        editTextDelegateIPAddress = (EditText) findViewById(R.id.editTextDelegateIPAddress);
        frameLayoutDelegateIPAddress = (FrameLayout) findViewById(R.id.frameLayoutDelegateIPAddress);
        editTextBlockVerifierMessagesPublicKey = (EditText) findViewById(R.id.editTextBlockVerifierMessagesPublicKey);
        frameLayoutBlockVerifierPublicKey = (FrameLayout) findViewById(R.id.frameLayoutBlockVerifierPublicKey);
        textViewTips = (TextView) findViewById(R.id.textViewTips);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onFocusChangeListener();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_dpops_register_textViewTitle_text);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void onFocusChangeListener() {
        onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                switch (view.getId()) {
                    case R.id.editTextDelegateName:
                        if (hasFocus) {
                            frameLayoutDelegateName.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutDelegateName.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextDelegateIPAddress:
                        if (hasFocus) {
                            frameLayoutDelegateIPAddress.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutDelegateIPAddress.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextBlockVerifierMessagesPublicKey:
                        if (hasFocus) {
                            frameLayoutBlockVerifierPublicKey.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutBlockVerifierPublicKey.setBackgroundColor(mainColorText);
                        }
                        break;
                    default:
                        break;
                }

            }
        };
        editTextDelegateName.setOnFocusChangeListener(onFocusChangeListener);
        editTextDelegateIPAddress.setOnFocusChangeListener(onFocusChangeListener);
        editTextBlockVerifierMessagesPublicKey.setOnFocusChangeListener(onFocusChangeListener);
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
                        doRegister(buttonNext);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void doRegister(final View view) {
        if (wallet == null) {
            return;
        }
        String delegateName = editTextDelegateName.getText().toString();
        if (delegateName.equals("")) {
            BaseActivity.showShortToast(DpopsRegisterActivity.this, getString(R.string.activity_dpops_register_confirmDelegateNameEmpty_tips));
            return;
        }
        String delegateIPAddress = editTextDelegateIPAddress.getText().toString();
        if (delegateIPAddress.equals("")) {
            BaseActivity.showShortToast(DpopsRegisterActivity.this, getString(R.string.activity_dpops_register_confirmDelegateIPAddressEmpty_tips));
            return;
        }
        String blockVerifierMessagesPublicKey = editTextBlockVerifierMessagesPublicKey.getText().toString();
        if (blockVerifierMessagesPublicKey.equals("")) {
            BaseActivity.showShortToast(DpopsRegisterActivity.this, getString(R.string.activity_dpops_register_confirmBlockVerifierMessagesPublicKeyEmpty_tips));
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(DpopsRegisterActivity.this, view);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            final String content = "{\"delegateName\":" + delegateName + ",\"delegateIPAddress\":" + delegateIPAddress + ",\"blockVerifierMessagesPublicKey\":" + blockVerifierMessagesPublicKey + "}\n\nResult=> ";
            walletOperateManager.delegateRegister(delegateName, delegateIPAddress, blockVerifierMessagesPublicKey, new OnNormalListener.Stub() {
                @Override
                public void onSuccess(final String tips) throws RemoteException {
                    WalletServiceHelper.addOperationHistory(wallet.getId(), "Delegate Register", true, content + tips);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTips.setText(tips);
                            textViewTips.setTextColor(colorPrimary);
                            ProgressDialogHelp.enabledView(DpopsRegisterActivity.this, progressDialog, progressDialogKey, view);
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    WalletServiceHelper.addOperationHistory(wallet.getId(), "Delegate Register", false, content + error);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTips.setText(error);
                            textViewTips.setTextColor(textView_error);
                            ProgressDialogHelp.enabledView(DpopsRegisterActivity.this, progressDialog, progressDialogKey, view);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(DpopsRegisterActivity.this, progressDialog, progressDialogKey, view);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
